package com.mymeds.userservice.service;

import com.mymeds.userservice.client.rest.AuthRegisterGuestClientRequest;
import com.mymeds.userservice.rest.GetGuestResponse;
import com.mymeds.userservice.rest.UpdateGuestRequest;
import java.util.List;
import java.util.UUID;

public interface GuestService {

  void updateGuest(UUID guestId, UpdateGuestRequest request);

  List<GetGuestResponse> getAllGuests(UUID userId);

  void createGuest(AuthRegisterGuestClientRequest request);
}
