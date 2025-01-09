package com.mymeds.medicationservice.persistance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "USER_MEDICATION")
@Entity
public class UserMedication extends AuditableEntity {

  @Column(name = "USER_MEDICATION_ID", columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID userMedicationId = UUID.randomUUID();

  @Column(name = "USER_ID", columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID userId;

  @ManyToOne
  @JoinColumn(name = "MEDICATION", nullable = false)
  private Medication medication;

  @ManyToOne
  @JoinColumn(name = "MEDICATION_DOSE")
  private MedicationDose medicationDose;

  @Column(name = "START_DATE", nullable = false)
  private LocalDate startDate;

  @Column(name = "END_DATE", nullable = false)
  private LocalDate endDate;

  @Column(name = "INSTRUCTIONS")
  private String instructions;
}
