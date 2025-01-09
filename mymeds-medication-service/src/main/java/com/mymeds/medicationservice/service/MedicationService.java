package com.mymeds.medicationservice.service;

import com.mymeds.medicationservice.rest.GetMedicationResponse;
import java.util.List;
import java.util.UUID;

public interface MedicationService {

  GetMedicationResponse getMedicationByMedicationId(UUID medicationId);

  List<GetMedicationResponse> getAllMedications();
}
