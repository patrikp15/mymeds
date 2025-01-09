package com.mymeds.userservice.client.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRegisterUserClientRequest {

  @NotNull
  private UUID userId;

  @NotNull
  private String firstName;

  private String middleName;

  @NotNull
  private String lastName;

  @NotNull
  private String mobileNumber;

  @NotNull
  @JsonFormat(locale = "YYYY-MM-DD")
  private LocalDate dateOfBirth;

  private AuthRegisterGuestClientRequest guestRequest;
}
