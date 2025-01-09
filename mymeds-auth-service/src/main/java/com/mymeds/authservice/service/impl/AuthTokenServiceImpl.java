package com.mymeds.authservice.service.impl;

import com.mymeds.authservice.client.NotificationClient;
import com.mymeds.authservice.enumeration.AuthTokenStatus;
import com.mymeds.authservice.enumeration.AuthTokenStep;
import com.mymeds.authservice.mapper.AuthTokenMapper;
import com.mymeds.authservice.mapper.UserDetailMapper;
import com.mymeds.authservice.persistance.dao.AuthTokenRepository;
import com.mymeds.authservice.persistance.dao.UserDetailRepository;
import com.mymeds.authservice.persistance.model.AuthToken;
import com.mymeds.authservice.persistance.model.UserDetail;
import com.mymeds.authservice.rest.GetUserDetailResponse;
import com.mymeds.authservice.rest.PasswordResetRequest;
import com.mymeds.authservice.rest.RegisterGuestRequest;
import com.mymeds.authservice.security.AuthPasswordEncoder;
import com.mymeds.authservice.security.JwtAuth;
import com.mymeds.authservice.security.JwtAuth.TokenGenerator;
import com.mymeds.authservice.service.AuthTokenService;
import com.mymeds.authservice.utils.DateUtils;
import com.mymeds.authservice.utils.MaskUtils;
import com.mymeds.authservice.utils.UserStatusTransition;
import com.mymeds.authservice.utils.Validation;
import com.mymeds.sharedutilities.enumeration.AuthTokenType;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import com.mymeds.sharedutilities.exception.AuthErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

  private static final Logger LOG = LoggerFactory.getLogger(AuthTokenServiceImpl.class);

  private final UserDetailRepository userDetailRepository;
  private final Validation validation;
  private final JwtAuth jwtAuth;
  private final AuthTokenRepository authTokenRepository;
  private final AuthTokenMapper authTokenMapper;
  private final AuthPasswordEncoder passwordEncoder;
  private final NotificationClient notificationClient;
  private final UserDetailMapper userDetailMapper;

  public AuthTokenServiceImpl(
      UserDetailRepository userDetailRepository,
      Validation validation,
      JwtAuth jwtAuth,
      AuthTokenRepository authTokenRepository,
      AuthTokenMapper authTokenMapper,
      AuthPasswordEncoder passwordEncoder,
      NotificationClient notificationClient,
      UserDetailMapper userDetailMapper
  ) {
    this.userDetailRepository = userDetailRepository;
    this.validation = validation;
    this.jwtAuth = jwtAuth;
    this.authTokenRepository = authTokenRepository;
    this.authTokenMapper = authTokenMapper;
    this.passwordEncoder = passwordEncoder;
    this.notificationClient = notificationClient;
    this.userDetailMapper = userDetailMapper;
  }

  @Transactional
  @Override
  public void resetPassword(PasswordResetRequest request) {
    LOG.info("action=resetPassword, status=started, step={}", request.getTokenStep());
    if (AuthTokenStep.NOTIFICATION_STEP.equals(request.getTokenStep())) {
      processPasswordResetNotificationStep(request);
    } else if(AuthTokenStep.VERIFICATION_STEP.equals(request.getTokenStep())) {
      processPasswordResetVerificationStep(request);
    }
    LOG.info("action=resetPassword, status=finished");
  }

  @Transactional
  @Override
  public void verifyUser(String token, AuthTokenStep tokenStep, UUID userId) {
    LOG.info("action=verifyUser, status=started, userId={}. tokenStep={}", userId, tokenStep);
    if (AuthTokenStep.NOTIFICATION_STEP.equals(tokenStep)) {
      processVerifyUserNotificationStep(userId);
    } else if(AuthTokenStep.VERIFICATION_STEP.equals(tokenStep)) {
      processVerifyUserVerificationStep(token);
    }
    LOG.info("action=verifyUser, status=finished");
  }

  @Override
  public AuthToken generateAndPersistAuthToken(
      TokenGenerator tokenGenerator,
      UserDetail userDetail,
      AuthTokenType tokenType
  ) {
    LOG.info("action=generateAndPersistAuthToken, status=started, tokenType={}", tokenType);
    final UUID userId = userDetail.getUserId();
    final String token = tokenGenerator.generate(userDetail);
    final AuthToken authToken = authTokenMapper.toAuthToken(
        token,
        userId,
        DateUtils.fromDateToLocalDateTime(jwtAuth.getExpirationDateFromToken(token)),
        tokenType
    );
    authTokenRepository.save(authToken);
    LOG.info("action=generateAndPersistAuthToken, status=finished");
    return authToken;
  }

  @Override
  public GetUserDetailResponse getUserDetailByToken(String token) {
    LOG.info("action=getUserDetailByToken, status=started");
    final AuthToken authToken = authTokenRepository.findOneByToken(token)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_BY_TOKEN_NOT_FOUND));
    final GetUserDetailResponse getUserDetailResponse = userDetailRepository.findOneByUserId(authToken.getUserId())
        .map(userDetailMapper::toGetUserDetailResponse)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));
    LOG.info("action=getUserDetailByToken, status=finished");
    return getUserDetailResponse;
  }

  @Override
  public void registerGuest(RegisterGuestRequest request, String token) {
    LOG.info("action=registerGuest, status=started");
    final AuthToken authToken = validateTokenAndSetAsExpired(token);
    userDetailRepository.findOneByUserId(authToken.getUserId()).ifPresentOrElse(
        guest -> {
          validation.validatePassword(request.getPassword(), request.getPasswordRe());
          guest.setPassword(passwordEncoder.encode(request.getPassword()));
          UserStatusTransition.build(guest.getUserStatus(), UserStatus.ACTIVE).validate();
          guest.setUserStatus(UserStatus.ACTIVE);
          userDetailRepository.save(guest);
        },
        () -> {
          throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
        }
    );
    LOG.info("action=registerGuest, status=finished");
  }

  @Override
  public void verifyGuest(UUID guestId) {
    final UserDetail guest = userDetailRepository.findOneByUserId(guestId)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));

    final TokenGenerator tokenGenerator = user -> jwtAuth.generateGuestRegistrationToken(
        guest.getUserId(),
        guest.getUserRole(),
        guest.getUserStatus()
    );
    final AuthToken authToken = generateAndPersistAuthToken(
        tokenGenerator, guest, AuthTokenType.GUEST_REGISTRATION
    );
    notificationClient.sendAuthNotification(guest.getEmail(), authToken);
  }

  private void processVerifyUserNotificationStep(UUID userId) {
    final UserDetail userDetail = userDetailRepository.findOneByUserId(userId)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_ID_NOT_FOUND));
    final TokenGenerator tokenGenerator = user -> jwtAuth.generateResetPasswordToken(
        user.getUserId(),
        user.getUserRole(),
        user.getUserStatus()
    );
    final AuthToken authToken = generateAndPersistAuthToken(
        tokenGenerator, userDetail, AuthTokenType.USER_VERIFICATION
    );
    notificationClient.sendAuthNotification(userDetail.getEmail(), authToken);
  }

  private void processVerifyUserVerificationStep(String token) {
    validateTokenAndSetAsExpired(token);
    jwtAuth.getUserIdFromJWT(token).ifPresentOrElse(
        id -> userDetailRepository.findOneByUserId(UUID.fromString(id)).ifPresentOrElse(
            user -> {
              user.setUserStatus(UserStatus.ACTIVE);
              userDetailRepository.save(user);
            },
            () -> {
              throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
            }
        ),
        () -> {
          throw new MyMedsGeneralException(UserErrorCode.USER_ID_NOT_FOUND);
        }
    );
  }

  private void processPasswordResetVerificationStep(PasswordResetRequest request) {
    LOG.info("action=processPasswordResetVerificationStep, status=started");
    final UUID userId = jwtAuth.getUserIdFromJWT(request.getToken()).map(UUID::fromString)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_ID_NOT_FOUND));

    validation.validatePassword(request.getPassword(), request.getPasswordRe());
    validateTokenAndSetAsExpired(request.getToken());

    userDetailRepository.findOneByUserId(userId).ifPresentOrElse(
        userDetail -> {
          userDetail.setPassword(passwordEncoder.encode(request.getPassword()));
          userDetailRepository.save(userDetail);
        },
        () -> {
          LOG.error("action=processPasswordResetVerificationStep, status=failed, message=user not found");
          throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
        }
    );
    LOG.info("action=processPasswordResetVerificationStep, status=finished");
  }

  private void processPasswordResetNotificationStep(PasswordResetRequest request) {
    final String email = request.getEmail();
    LOG.info("action=processPasswordResetNotificationStep, status=started, email={}", MaskUtils.mask(email));
    userDetailRepository.findOneByEmail(email).ifPresentOrElse(
        userDetail -> {
          final TokenGenerator tokenGenerator = user -> jwtAuth.generateResetPasswordToken(
              user.getUserId(),
              user.getUserRole(),
              user.getUserStatus()
          );
          final AuthToken authToken = generateAndPersistAuthToken(
              tokenGenerator, userDetail, AuthTokenType.PASSWORD_RESET
          );
          notificationClient.sendAuthNotification(userDetail.getEmail(), authToken);
        },
        () -> {
          LOG.error("action=processPasswordResetNotificationStep, status=failed, message=user not found");
          throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
        }
    );
    LOG.info("action=processPasswordResetNotificationStep, status=finished");
  }

  private AuthToken validateTokenAndSetAsExpired(String token) {
    final AuthToken authToken = authTokenRepository.findOneByToken(token)
        .filter(this::checkTokenActive)
        .filter(this::checkTokenNotExpired)
        .orElseThrow(() -> new MyMedsGeneralException(AuthErrorCode.AUTH_TOKEN_NOT_FOUND));

    jwtAuth.validate(authToken.getToken());
    authToken.setTokenStatus(AuthTokenStatus.NOT_ACTIVE);
    return authTokenRepository.save(authToken);
  }

  private boolean checkTokenActive(AuthToken token) {
    if (AuthTokenStatus.NOT_ACTIVE.equals(token.getTokenStatus())) {
      throw new MyMedsGeneralException(AuthErrorCode.AUTH_TOKEN_ALREADY_USED);
    }
    return true;
  }

  private boolean checkTokenNotExpired(AuthToken token) {
    if (LocalDateTime.now().isAfter(token.getExpireAt())) {
      throw new MyMedsGeneralException(AuthErrorCode.AUTH_TOKEN_EXPIRED);
    }
    return true;
  }
}
