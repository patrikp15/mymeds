package com.mymeds.userservice.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.UserRole;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserResponse {

  private UUID userId;

  private String firstName;

  private String middleName;

  private String lastName;

  private String email;

  private String mobileNumber;

  @JsonFormat(locale = "YYYY-MM-DD")
  private LocalDate dateOfBirth;

  private UserRole userRole;

  private UserStatus userStatus;

  private List<GetGuestResponse> guests;
}
