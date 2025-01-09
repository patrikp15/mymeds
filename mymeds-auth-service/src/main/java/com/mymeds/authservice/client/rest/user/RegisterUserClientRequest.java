package com.mymeds.authservice.client.rest.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonInclude(Include.NON_EMPTY)
public class RegisterUserClientRequest implements Serializable {

  private UUID userId;

  private String firstName;

  private String middleName;

  private String lastName;

  private String mobileNumber;

  private LocalDate dateOfBirth;

  private RegisterGustClientRequest guestRequest;
}
