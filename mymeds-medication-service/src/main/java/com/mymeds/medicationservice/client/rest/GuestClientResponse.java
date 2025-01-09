package com.mymeds.medicationservice.client.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.RelationshipType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestClientResponse {

  private String firstName;

  private String middleName;

  private String lastName;

  private String email;

  private String mobileNumber;

  private RelationshipType relationshipType;
}
