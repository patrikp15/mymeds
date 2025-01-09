package com.mymeds.medicationservice.service;

import com.mymeds.medicationservice.enumeration.RestDateFilter;
import com.mymeds.medicationservice.rest.CreateUserMedicationRequest;
import com.mymeds.medicationservice.rest.GetUserMedicationResponse;
import java.util.List;
import java.util.UUID;

public interface UserMedicationService {

  void addUserMedication(CreateUserMedicationRequest request);

  void deleteUserMedicationById(UUID userMedicationId);

  List<GetUserMedicationResponse> getUserMedications();

  List<GetUserMedicationResponse> getUserMedicationsByUserId(UUID uid);

  List<GetUserMedicationResponse> getUserMedicationsByDateFilter(RestDateFilter dateFilter);

  List<GetUserMedicationResponse> getUserMedicationsByTextFilter(String textFilter);
}
