package com.mymeds.userservice.service.impl;

import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import com.mymeds.userservice.client.AuthClient;
import com.mymeds.userservice.client.rest.AuthUserDetailClientResponse;
import com.mymeds.userservice.mapper.UserDetailMapper;
import com.mymeds.userservice.persistance.dao.GuestRepository;
import com.mymeds.userservice.persistance.dao.UserDetailRepository;
import com.mymeds.userservice.persistance.model.UserDetail;
import com.mymeds.userservice.rest.GetGuestResponse;
import com.mymeds.userservice.rest.GetUserResponse;
import com.mymeds.userservice.rest.UpdateUserRequest;
import com.mymeds.userservice.service.UserDetailService;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

  private static final Logger LOG = LoggerFactory.getLogger(UserDetailServiceImpl.class);

  private final UserDetailRepository userDetailRepository;
  private final GuestRepository guestRepository;
  private final UserDetailMapper userDetailMapper;
  private final AuthClient authClient;

  public UserDetailServiceImpl(
      UserDetailRepository userDetailRepository,
      GuestRepository guestRepository,
      UserDetailMapper userDetailMapper,
      AuthClient authClient
  ) {
    this.userDetailRepository = userDetailRepository;
    this.guestRepository = guestRepository;
    this.userDetailMapper = userDetailMapper;
    this.authClient = authClient;
  }

  @Override
  public GetUserResponse getUserById(UUID userId) {
    LOG.info("action=getUserById, status=started, id={}", userId);
    final UserDetail userDetail = userDetailRepository.findOneByUserId(userId)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));

    AuthUserDetailClientResponse authClientUserDetail = authClient.getUserDetailById(userId);

    final GetUserResponse getUserResponse = userDetailMapper.toGetUserDetailResponse(
        userDetail,
        authClientUserDetail,
        List.of()
    );

    LOG.info("action=getUserById, status=finished");
    return getUserResponse;
  }

  @Override
  public GetUserResponse getUserWithGuestsById(UUID userId) {
    LOG.info("action=getUserWithGuestsById, status=started, id={}", userId);
    final UserDetail userDetail = userDetailRepository.findOneByUserId(userId)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));

    final GetUserResponse getUserResponse = userDetailMapper.toGetUserDetailResponse(
        userDetail,
        authClient.getUserDetailById(userId),
        getAllGuests(userId)
    );

    LOG.info("action=getUserWithGuestsById, status=finished");
    return getUserResponse;
  }

  @Override
  public void updateUser(UUID userId, UpdateUserRequest request) {
    LOG.info("action=updateUser, status=started, id={}", userId);
    userDetailRepository.findOneByUserId(userId).ifPresentOrElse(
        userDetail -> {
          if (request.getMobileNumber() != null) {
            // TODO validate mobile number
            userDetail.setMobileNumber(request.getMobileNumber());
          }
          userDetailRepository.save(userDetail);
        },
        () -> {
          throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
        });
    LOG.info("action=updateUser, status=finished");
  }

  private List<GetGuestResponse> getAllGuests(UUID userId) {
    return guestRepository.findAllByUserDetailUserId(userId).stream().map(
        guest -> {
          final AuthUserDetailClientResponse authGuestRes = authClient.getUserDetailById(guest.getGuestId());
          return userDetailMapper.toGetGuestResponse(guest, authGuestRes);
        }
    ).toList();
  }
}