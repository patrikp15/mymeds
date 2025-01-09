package com.mymeds.medicationservice.mapper;

import com.mymeds.medicationservice.persistance.model.Medication;
import com.mymeds.medicationservice.persistance.model.MedicationDose;
import com.mymeds.medicationservice.persistance.model.UserMedication;
import com.mymeds.medicationservice.rest.CreateUserMedicationRequest;
import com.mymeds.medicationservice.rest.GetMedication;
import com.mymeds.medicationservice.rest.GetUserMedicationResponse;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMedicationMapper {

  @Mapping(target = "medication", source = "medication")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdTs", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  @Mapping(target = "modifiedTs", ignore = true)
  UserMedication toUserMedication(CreateUserMedicationRequest request, UUID userId, Medication medication, MedicationDose medicationDose);

  @Mapping(target = "medication", expression = "java(toGetMedication(userMedication.getMedication(), userMedication.getMedicationDose()))")
  GetUserMedicationResponse toGetUserMedication(UserMedication userMedication);

  @Mapping(source = "medicationDose.dose", target = "dose")
  @Mapping(source = "medicationDose.frequency", target = "frequency")
  @Mapping(source = "medicationDose.description", target = "doseDescription")
  @Mapping(source = "medication.description", target = "description")
  GetMedication toGetMedication(Medication medication, MedicationDose medicationDose);
}
