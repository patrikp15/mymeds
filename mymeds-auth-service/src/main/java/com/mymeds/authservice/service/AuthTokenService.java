package com.mymeds.authservice.service;

import com.mymeds.authservice.enumeration.AuthTokenStep;
import com.mymeds.authservice.persistance.model.AuthToken;
import com.mymeds.authservice.persistance.model.UserDetail;
import com.mymeds.authservice.rest.GetUserDetailResponse;
import com.mymeds.authservice.rest.PasswordResetRequest;
import com.mymeds.authservice.rest.RegisterGuestRequest;
import com.mymeds.authservice.security.JwtAuth.TokenGenerator;
import com.mymeds.sharedutilities.enumeration.AuthTokenType;
import java.util.UUID;

public interface AuthTokenService {

  void resetPassword(PasswordResetRequest request);

  void verifyUser(String token, AuthTokenStep tokenStep, UUID userId);

  AuthToken generateAndPersistAuthToken(
      TokenGenerator tokenGenerator,
      UserDetail userDetail,
      AuthTokenType tokenType
  );

  GetUserDetailResponse getUserDetailByToken(String token);

  void registerGuest(RegisterGuestRequest request, String token);

  void verifyGuest(UUID guestId);
}
