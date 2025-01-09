package com.mymeds.authservice.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.UserRole;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateTokenResponse {

  private UUID userId;

  private UserRole userRole;

  private UserStatus userStatus;

  private LocalDateTime expiration;
}
