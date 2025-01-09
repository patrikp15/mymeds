package com.mymeds.authservice.service;

import com.mymeds.authservice.rest.AuthUserResponse;
import com.mymeds.authservice.rest.LoginUserRequest;
import com.mymeds.authservice.rest.RegisterUserRequest;
import com.mymeds.authservice.rest.ValidateTokenResponse;

public interface AuthService {

  AuthUserResponse registerUser(RegisterUserRequest request);

  AuthUserResponse loginUser(LoginUserRequest request);

  ValidateTokenResponse validateJwtToken(String token);
}
