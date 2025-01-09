package com.mymeds.userservice.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.RelationshipType;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetGuestResponse {

  private UUID guestId;

  private String firstName;

  private String middleName;

  private String lastName;

  private String email;

  private String mobileNumber;

  private RelationshipType relationshipType;

  private String userStatus;

  private String userRole;
}
