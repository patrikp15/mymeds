package com.mymeds.authservice.client.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailClient {

  private String firstName;

  private String middleName;

  private String lastName;
}
