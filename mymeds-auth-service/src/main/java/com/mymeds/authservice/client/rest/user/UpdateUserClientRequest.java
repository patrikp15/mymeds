package com.mymeds.authservice.client.rest.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonInclude(Include.NON_EMPTY)
public class UpdateUserClientRequest {

  private String mobileNumber;

  private String relationshipType;
}
