package com.mymeds.authservice.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeUserStatusRequest {

  @NotNull
  private UserStatus userStatus;
}
