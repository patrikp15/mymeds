package com.mymeds.authservice.client.rest.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonInclude(Include.NON_EMPTY)
public class RegisterGustClientRequest implements Serializable {

  private UUID userId;

  private UUID guestId;

  private String firstName;

  private String middleName;

  private String lastName;

  private String mobileNumber;

  private String relationshipType;
}
