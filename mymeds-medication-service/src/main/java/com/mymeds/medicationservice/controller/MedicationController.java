package com.mymeds.medicationservice.controller;

import com.mymeds.medicationservice.rest.GetMedicationResponse;
import com.mymeds.medicationservice.service.MedicationService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medications")
public class MedicationController {

  private final MedicationService medicationService;

  public MedicationController(MedicationService medicationService) {
    this.medicationService = medicationService;
  }

  @GetMapping("/{medicationId}")
  public ResponseEntity<GetMedicationResponse> getMedication(@PathVariable UUID medicationId) {
    return ResponseEntity.ok(medicationService.getMedicationByMedicationId(medicationId));
  }

  @GetMapping
  public ResponseEntity<List<GetMedicationResponse>> getAllMedications() {
    return ResponseEntity.ok(medicationService.getAllMedications());
  }
}
