package com.mymeds.apigateway.service;

import com.mymeds.apigateway.rest.ValidateTokenResponse;

public interface AuthService {

  ValidateTokenResponse validateJwtToken(String token);
}
