package com.mymeds.authservice.client.rest.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.AuthTokenType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthNotificationRequest {

  private String email;

  private AuthTokenType tokenType;

  private String token;

  private LocalDateTime tokenExpiration;
}
