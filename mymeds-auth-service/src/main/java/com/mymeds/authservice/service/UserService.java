package com.mymeds.authservice.service;

import com.mymeds.authservice.rest.ChangeUserStatusRequest;
import com.mymeds.authservice.rest.CreateGuestRequest;
import com.mymeds.authservice.rest.GetUserDetailResponse;
import com.mymeds.authservice.rest.UpdateUserRequest;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  void updateUser(UUID userId, UpdateUserRequest request);

  GetUserDetailResponse getUserDetailById(UUID userId);

  void changeUserStatus(UUID userId, ChangeUserStatusRequest request);

  void createGuest(UUID userId, CreateGuestRequest request);
}
