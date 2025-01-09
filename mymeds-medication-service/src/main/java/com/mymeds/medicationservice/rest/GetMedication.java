package com.mymeds.medicationservice.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mymeds.medicationservice.enumeration.MedicationRoute;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class GetMedication {

  private UUID medicationId;

  private String name;

  private String description;

  private MedicationRoute route;

  private String dose;

  private String frequency;

  private String doseDescription;
}