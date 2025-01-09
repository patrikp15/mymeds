package com.mymeds.userservice.service.impl;

import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import com.mymeds.userservice.client.AuthClient;
import com.mymeds.userservice.client.rest.AuthRegisterGuestClientRequest;
import com.mymeds.userservice.mapper.GuestMapper;
import com.mymeds.userservice.persistance.dao.GuestRepository;
import com.mymeds.userservice.persistance.dao.UserDetailRepository;
import com.mymeds.userservice.persistance.model.Guest;
import com.mymeds.userservice.persistance.model.UserDetail;
import com.mymeds.userservice.rest.GetGuestResponse;
import com.mymeds.userservice.rest.UpdateGuestRequest;
import com.mymeds.userservice.service.GuestService;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GuestServiceImpl implements GuestService {

  private static final Logger LOG = LoggerFactory.getLogger(GuestServiceImpl.class);

  private static final int MAX_NUMBER_OF_GUESTS = 4;

  private final GuestRepository guestRepository;
  private final UserDetailRepository userDetailRepository;
  private final GuestMapper guestMapper;
  private final AuthClient authClient;

  public GuestServiceImpl(
      GuestRepository guestRepository,
      UserDetailRepository userDetailRepository,
      GuestMapper guestMapper,
      AuthClient authClient
  ) {
    this.guestRepository = guestRepository;
    this.userDetailRepository = userDetailRepository;
    this.guestMapper = guestMapper;
    this.authClient = authClient;
  }

  @Override
  public void createGuest(AuthRegisterGuestClientRequest request) {
    final UUID userId = request.getUserId();
    LOG.info("action=createGuest, status=started, userId={}, guestId={}", userId, request.getGuestId());

    final UserDetail userDetail = userDetailRepository.findOneByUserId(userId)
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));
    guardMaxNumberOfGuests(userId);

    final Guest guest = guestMapper.toGuest(request);
    guest.setUserDetail(userDetail);
    guestRepository.save(guest);

    LOG.info("action=createGuest, status=finished");
  }

  @Override
  public void updateGuest(UUID guestId, UpdateGuestRequest request) {
    LOG.info("action=updateGuest, status=started, id={}", guestId);
    guestRepository.findOneByGuestId(guestId)
        .ifPresentOrElse(guest -> {
              if (request.getMobileNumber() != null) {
                // TODO validate mobile number
                guest.setMobileNumber(request.getMobileNumber());
              }
              if (request.getRelationshipType() != null) {
                guest.setRelationshipType(request.getRelationshipType());
              }
              guestRepository.save(guest);
            },
            () -> {
              throw new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND);
            });
    LOG.info("action=updateGuest, status=finished");
  }

  @Override
  public List<GetGuestResponse> getAllGuests(UUID userId) {
    LOG.info("action=getAllGuests, status=started, userId={}", userId);
    final List<GetGuestResponse> guestResponses = guestRepository.findAllByUserDetailUserId(userId).stream()
        .map(guest -> guestMapper.toGetGuestResponse(guest, authClient.getUserDetailById(guest.getGuestId())))
        .toList();
    LOG.info("action=getAllGuests, status=finished");
    return guestResponses;
  }

  private void guardMaxNumberOfGuests(UUID userId) {
    int size = guestRepository.findAllByUserDetailUserId(userId).size();
    if (size >= MAX_NUMBER_OF_GUESTS) {
      LOG.error("action=guardMaxNumberOfGuests, status=failed, guestsSize={}", size);
      throw new MyMedsGeneralException(UserErrorCode.MAX_NUMBER_OF_GUESTS_EXCEEDED);
    }
  }
}
