package com.mymeds.authservice.service.impl;

import com.mymeds.authservice.client.MedicationClient;
import com.mymeds.authservice.client.NotificationClient;
import com.mymeds.authservice.client.UserClient;
import com.mymeds.authservice.mapper.UserDetailMapper;
import com.mymeds.authservice.persistance.dao.UserDetailRepository;
import com.mymeds.authservice.persistance.model.AuthToken;
import com.mymeds.authservice.persistance.model.UserDetail;
import com.mymeds.authservice.rest.AuthUserResponse;
import com.mymeds.authservice.rest.CreateGuestRequest;
import com.mymeds.authservice.rest.LoginUserRequest;
import com.mymeds.authservice.rest.RegisterUserRequest;
import com.mymeds.authservice.rest.ValidateTokenResponse;
import com.mymeds.authservice.security.AuthPasswordEncoder;
import com.mymeds.authservice.security.JwtAuth;
import com.mymeds.authservice.security.JwtAuth.TokenGenerator;
import com.mymeds.authservice.service.AuthService;
import com.mymeds.authservice.service.AuthTokenService;
import com.mymeds.authservice.utils.DateUtils;
import com.mymeds.authservice.utils.MaskUtils;
import com.mymeds.authservice.utils.Validation;
import com.mymeds.sharedutilities.enumeration.AuthTokenType;
import com.mymeds.sharedutilities.enumeration.UserRole;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import com.mymeds.sharedutilities.exception.AuthErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

  private static final Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

  private final UserDetailRepository userDetailRepository;
  private final Validation validation;
  private final UserDetailMapper userDetailMapper;
  private final AuthPasswordEncoder passwordEncoder;
  private final JwtAuth jwtAuth;
  private final UserClient userClient;
  private final MedicationClient medicationClient;
  private final AuthTokenService authTokenService;
  private final NotificationClient notificationClient;

  public AuthServiceImpl(
      UserDetailRepository userDetailRepository,
      Validation validation,
      UserDetailMapper userDetailMapper,
      AuthPasswordEncoder passwordEncoder,
      JwtAuth jwtAuth,
      UserClient userClient,
      MedicationClient medicationClient,
      AuthTokenService authTokenService,
      NotificationClient notificationClient
  ) {
    this.userDetailRepository = userDetailRepository;
    this.validation = validation;
    this.userDetailMapper = userDetailMapper;
    this.passwordEncoder = passwordEncoder;
    this.jwtAuth = jwtAuth;
    this.userClient = userClient;
    this.medicationClient = medicationClient;
    this.authTokenService = authTokenService;
    this.notificationClient = notificationClient;
  }

  @Transactional
  @Override
  public AuthUserResponse registerUser(RegisterUserRequest request) {
    LOG.info("action=registerUser, status=started, user={}", MaskUtils.mask(request.getEmail()));

    validation.guardEmailDoesNotExist(() -> userDetailRepository.existsByEmail(request.getEmail()));
    validation.validateEmailAddress(request.getEmail());
    validation.validatePassword(request.getPassword(), request.getPasswordRep());

    final UserDetail userDetail = userDetailMapper.toUserDetail(request, UserRole.BASIC, UserStatus.NOT_VERIFIED);
    userDetail.setPassword(passwordEncoder.encode(request.getPassword()));
    final UserDetail savedUserDetail = userDetailRepository.save(userDetail);

    final UserDetail guest = persistGuest(request.getGuestRequest());

    userClient.sendUserRegistrationDetailsToUserService(request, userDetail, guest);
    medicationClient.generateQRCode(savedUserDetail.getUserId());
    sendVerifyUserNotification(userDetail);
    sendGuestRegistrationNotification(guest);

    LOG.info("action=registerUser, status=finished");
    return userDetailMapper.toAuthUserResponse(savedUserDetail);
  }

  @Override
  public AuthUserResponse loginUser(LoginUserRequest request) {
    LOG.info("action=loginUser, status=started, user={}", MaskUtils.mask(request.getUserName()));
    final UserDetail userDetail = userDetailRepository.findOneByEmail(request.getUserName())
        .orElseThrow(() -> {
          LOG.error("action=loginUser, status=failed, message=User not found");
          return new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
        });

    if (!passwordEncoder.isValid(request.getPassword(), userDetail.getPassword())) {
      LOG.error("action=authenticate, status=failed, message=Incorrect password");
      throw new MyMedsGeneralException(UserErrorCode.PASSWORD_INVALID);
    }
    LOG.info("action=loginUser, status=finished");
    return userDetailMapper.toAuthUserResponse(userDetail);
  }

  @Override
  public ValidateTokenResponse validateJwtToken(String token) {
    jwtAuth.validate(token);

    final UUID userId = jwtAuth.getUserIdFromJWT(token)
        .map(user -> userDetailRepository.findOneByUserId(UUID.fromString(user)))
        .flatMap(Function.identity())
        .map(UserDetail::getUserId)
        .orElseThrow(() -> new MyMedsGeneralException(AuthErrorCode.AUTH_FAILED));

    final Optional<UserRole> userRole = jwtAuth.getFromToken(token, UserRole.class, "userRole");
    final Optional<UserStatus> userStatus = jwtAuth.getFromToken(token, UserStatus.class, "userStatus");
    return ValidateTokenResponse.builder()
        .userId(userId)
        .userRole(userRole.orElse(null))
        .userStatus(userStatus.orElse(null))
        .expiration(DateUtils.fromDateToLocalDateTime(jwtAuth.getExpirationDateFromToken(token)))
        .build();
  }

  private UserDetail persistGuest(CreateGuestRequest request) {
    final UserDetail guestUserDetail = userDetailMapper.toGuestUserDetail(
        request,
        UserRole.GUEST,
        UserStatus.NOT_VERIFIED
    );
    return userDetailRepository.save(guestUserDetail);
  }

  private void sendVerifyUserNotification(UserDetail userDetail) {
    final TokenGenerator tokenGenerator = user -> jwtAuth.generateResetPasswordToken(
        user.getUserId(),
        user.getUserRole(),
        user.getUserStatus()
    );
    final AuthToken authToken = authTokenService.generateAndPersistAuthToken(
        tokenGenerator, userDetail, AuthTokenType.USER_VERIFICATION
    );
    notificationClient.sendAuthNotification(userDetail.getEmail(), authToken);
  }

  private void sendGuestRegistrationNotification(UserDetail guest) {
    final TokenGenerator tokenGenerator = user -> jwtAuth.generateGuestRegistrationToken(
        guest.getUserId(),
        guest.getUserRole(),
        guest.getUserStatus()
    );
    final AuthToken authToken = authTokenService.generateAndPersistAuthToken(
        tokenGenerator, guest, AuthTokenType.GUEST_REGISTRATION
    );
    notificationClient.sendAuthNotification(guest.getEmail(), authToken);
  }
}
