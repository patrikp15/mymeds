package com.mymeds.userservice.client.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.RelationshipType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRegisterGuestClientRequest {

  @NotNull
  private UUID userId;

  @NotNull
  private UUID guestId;

  @NotNull
  private String firstName;

  private String middleName;

  @NotNull
  private String lastName;

  @NotNull
  private String mobileNumber;

  @NotNull
  private RelationshipType relationshipType;
}
