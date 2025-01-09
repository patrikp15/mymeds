package com.mymeds.medicationservice.client.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.UserRole;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUserClientResponse {

  private String email;

  private UserStatus userStatus;

  private UserRole userRole;
}
