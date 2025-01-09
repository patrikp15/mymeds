package com.mymeds.authservice.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterUserRequest {

  @NotNull
  @Email
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String passwordRep;

  @NotNull
  private String firstName;

  private String middleName;

  @NotNull
  private String lastName;

  @NotNull
  private String mobileNumber;

  @NotNull
  private LocalDate dateOfBirth;

  private CreateGuestRequest guestRequest;
}
