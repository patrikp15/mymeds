package com.mymeds.medicationservice.controller;

import com.mymeds.medicationservice.rest.GetUserMedicationResponse;
import com.mymeds.medicationservice.service.UserMedicationService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

  private final UserMedicationService userMedicationService;

  public PublicController(UserMedicationService userMedicationService) {
    this.userMedicationService = userMedicationService;
  }

  @GetMapping("/user-medications")
  public ResponseEntity<List<GetUserMedicationResponse>> getUserMedicationsByUserId(
      @RequestParam UUID uid
  ) {
    return ResponseEntity.ok(userMedicationService.getUserMedicationsByUserId(uid));
  }
}
