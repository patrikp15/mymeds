package com.mymeds.authservice.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.authservice.enumeration.AuthTokenStep;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordResetRequest {

  @NotNull
  AuthTokenStep tokenStep;

  @NotNull
  @Email
  private String email;

  private String token;

  private String password;

  private String passwordRe;
}
