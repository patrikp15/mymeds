package com.mymeds.userservice.service.impl;

import com.mymeds.userservice.client.rest.AuthRegisterGuestClientRequest;
import com.mymeds.userservice.client.rest.AuthRegisterUserClientRequest;
import com.mymeds.userservice.mapper.GuestMapper;
import com.mymeds.userservice.mapper.UserDetailMapper;
import com.mymeds.userservice.persistance.dao.GuestRepository;
import com.mymeds.userservice.persistance.dao.UserDetailRepository;
import com.mymeds.userservice.persistance.model.Guest;
import com.mymeds.userservice.persistance.model.UserDetail;
import com.mymeds.userservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

  private static final Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

  private final UserDetailRepository userDetailRepository;
  private final GuestRepository guestRepository;
  private final UserDetailMapper userDetailMapper;
  private final GuestMapper guestMapper;

  public AuthServiceImpl(
      UserDetailRepository userDetailRepository,
      GuestRepository guestRepository,
      UserDetailMapper userDetailMapper,
      GuestMapper guestMapper) {
    this.userDetailRepository = userDetailRepository;
    this.guestRepository = guestRepository;
    this.userDetailMapper = userDetailMapper;
    this.guestMapper = guestMapper;
  }

  @Transactional
  @Override
  public void registerUser(AuthRegisterUserClientRequest request) {
    LOG.info("action=registerUser, status=started");

    final UserDetail userDetail = userDetailMapper.toUserDetail(request);
    userDetailRepository.save(userDetail);

    final AuthRegisterGuestClientRequest guestRequest = request.getGuestRequest();
    final Guest guest = guestMapper.toGuest(guestRequest);
    guest.setUserDetail(userDetail);
    guestRepository.save(guest);

    LOG.info("action=registerUser, status=finished");
  }
}
