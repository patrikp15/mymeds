package com.mymeds.medicationservice.client.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserClientResponse {

  private String firstName;

  private String middleName;

  private String lastName;

  private String email;

  private String mobileNumber;

  private LocalDate dateOfBirth;

  private List<GuestClientResponse> guests;
}
