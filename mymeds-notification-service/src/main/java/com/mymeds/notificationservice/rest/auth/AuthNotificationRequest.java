package com.mymeds.notificationservice.rest.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.notificationservice.enumeration.AuthTokenType;
import com.mymeds.notificationservice.rest.UserDetail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthNotificationRequest {

  @NotNull
  @Email
  private String email;

  @NotNull
  private AuthTokenType tokenType;

  private String token;

  private LocalDateTime tokenExpiration;
}
