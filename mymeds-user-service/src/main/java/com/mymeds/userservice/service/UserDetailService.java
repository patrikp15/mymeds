package com.mymeds.userservice.service;

import com.mymeds.userservice.rest.GetUserResponse;
import com.mymeds.userservice.rest.UpdateUserRequest;
import java.util.UUID;

public interface UserDetailService {

  GetUserResponse getUserById(UUID userId);

  GetUserResponse getUserWithGuestsById(UUID userId);

  void updateUser(UUID userId, UpdateUserRequest request);
}
