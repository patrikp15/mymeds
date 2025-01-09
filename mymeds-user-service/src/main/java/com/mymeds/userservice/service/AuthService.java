package com.mymeds.userservice.service;

import com.mymeds.userservice.client.rest.AuthRegisterUserClientRequest;

public interface AuthService {

  void registerUser(AuthRegisterUserClientRequest request);
}
