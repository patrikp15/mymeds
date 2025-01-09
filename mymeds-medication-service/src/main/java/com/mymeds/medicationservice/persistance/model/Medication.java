package com.mymeds.medicationservice.persistance.model;

import com.mymeds.medicationservice.enumeration.MedicationRoute;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "MEDICATION")
@Entity
public class Medication extends AuditableEntity {

  @Column(name = "MEDICATION_ID", columnDefinition = "UUID", updatable = false, nullable = false, unique = true)
  private UUID medicationId = UUID.randomUUID();

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "DESCRIPTION", nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "ROUTE", nullable = false)
  private MedicationRoute route;
}
