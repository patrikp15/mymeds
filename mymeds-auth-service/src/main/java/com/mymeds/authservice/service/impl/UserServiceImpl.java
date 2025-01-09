package com.mymeds.authservice.service.impl;

import com.mymeds.authservice.client.UserClient;
import com.mymeds.authservice.mapper.UserDetailMapper;
import com.mymeds.authservice.persistance.dao.UserDetailRepository;
import com.mymeds.authservice.persistance.model.UserDetail;
import com.mymeds.authservice.rest.ChangeUserStatusRequest;
import com.mymeds.authservice.rest.CreateGuestRequest;
import com.mymeds.authservice.rest.GetUserDetailResponse;
import com.mymeds.authservice.rest.UpdateUserRequest;
import com.mymeds.authservice.service.UserService;
import com.mymeds.authservice.utils.UserStatusTransition;
import com.mymeds.authservice.utils.Validation;
import com.mymeds.sharedutilities.enumeration.UserRole;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserDetailRepository userDetailRepository;
  private final Validation validation;
  private final UserClient userClient;
  private final UserDetailMapper userDetailMapper;

  public UserServiceImpl(
      UserDetailRepository userDetailRepository,
      Validation validation,
      UserClient userClient,
      UserDetailMapper userDetailMapper
  ) {
    this.userDetailRepository = userDetailRepository;
    this.validation = validation;
    this.userClient = userClient;
    this.userDetailMapper = userDetailMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOG.info("action=loadUserByUsername, status=started, username={}", username);
    final UserDetail userDetail = userDetailRepository.findOneByEmail(username)
        .orElseThrow(() -> {
          LOG.error("action=loginUser, status=failed, message=User not found");
          return new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
        });
    LOG.info("action=loadUserByUsername, status=finished");
    return userDetail;
  }

  @Transactional
  @Override
  public void updateUser(UUID userId, UpdateUserRequest request) {
    LOG.info("action=updateUser, status=started, id={}", userId);
    userDetailRepository.findOneByUserId(userId).ifPresentOrElse(
        userDetail -> {
          guardUserActive(userDetail);
          if (request.getEmail() != null) {
            final String email = request.getEmail();
            validation.validateEmailAddress(email);
            userDetail.setEmail(email);
          }
          userDetailRepository.save(userDetail);
          userClient.sendUserUpdateUserToUserService(userId, userDetail.getUserRole(), request);
        },
        () -> {
          throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
        });

    LOG.info("action=updateUser, status=finished");
  }

  @Override
  public GetUserDetailResponse getUserDetailById(UUID userId) {
    LOG.info("action=getUserDetailById, status=started, userId={}", userId);
    final GetUserDetailResponse getUserDetailResponse = userDetailRepository.findOneByUserId(userId)
        .map(userDetailMapper::toGetUserDetailResponse)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));
    LOG.info("action=getUserDetailById, status=finished");
    return getUserDetailResponse;
  }

  @Override
  public void changeUserStatus(UUID userId, ChangeUserStatusRequest request) {
    final UserStatus toUserStatus = request.getUserStatus();
    LOG.info("action=changeUserStatus, status=started, userId={}", userId);

    userDetailRepository.findOneByUserId(userId).ifPresentOrElse(
        userDetail -> {
          LOG.info("action=changeUserStatus, fromStatus={}, toStatus={}", userDetail.getUserStatus(), toUserStatus);
          guardUserActive(userDetail);
          UserStatusTransition.build(userDetail.getUserStatus(), toUserStatus).validate();
          userDetail.setUserStatus(toUserStatus);
          userDetailRepository.save(userDetail);
        },
        () -> {
          throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
        }
    );
    LOG.info("action=changeUserStatus, status=finished");
  }

  @Transactional
  @Override
  public void createGuest(UUID userId, CreateGuestRequest request) {
    LOG.info("action=createGuest, status=started, userId={}", userId);

    userDetailRepository.findOneByUserId(userId)
        .ifPresentOrElse(this::guardUserActive,
            () -> {
              throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
            }
        );

    validation.validateEmailAddress(request.getEmail());
    final UserDetail guest = userDetailMapper.toUserDetail(request, UserRole.GUEST, UserStatus.NOT_VERIFIED);
    final UserDetail savedGuest = userDetailRepository.save(guest);

    userClient.sendCreateGuestDetailsToUserService(userId, savedGuest.getUserId(), request);

    LOG.info("action=createGuest, status=finished");
  }

  private void guardUserActive(UserDetail userDetail) {
    if (!UserStatus.ACTIVE.equals(userDetail.getUserStatus())) {
      throw new MyMedsGeneralException(UserErrorCode.USER_MUST_BE_ACTIVE);
    }
  }
}
