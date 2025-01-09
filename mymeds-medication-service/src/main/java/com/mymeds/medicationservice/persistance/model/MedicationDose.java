package com.mymeds.medicationservice.persistance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "MEDICATION_DOSE")
@Entity
public class MedicationDose extends AuditableEntity {

  @Column(name = "MEDICATION_DOSE_ID", columnDefinition = "UUID", updatable = false, nullable = false, unique = true)
  private UUID medicationDoseId = UUID.randomUUID();

  @ManyToOne
  @JoinColumn(name = "MEDICATION", nullable = false)
  private Medication medication;

  @Column(name = "DOSE", nullable = false)
  private String dose;

  @Column(name = "FREQUENCY", nullable = false)
  private String frequency;

  @Column(name = "DESCRIPTION")
  private String description;
}
