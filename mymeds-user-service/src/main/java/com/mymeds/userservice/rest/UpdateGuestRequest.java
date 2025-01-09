package com.mymeds.userservice.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymeds.sharedutilities.enumeration.RelationshipType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateGuestRequest {

  private String mobileNumber;

  private RelationshipType relationshipType;
}
