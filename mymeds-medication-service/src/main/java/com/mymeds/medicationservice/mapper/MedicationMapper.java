package com.mymeds.medicationservice.mapper;

import com.mymeds.medicationservice.persistance.model.Medication;
import com.mymeds.medicationservice.persistance.model.MedicationDose;
import com.mymeds.medicationservice.rest.GetMedicationDose;
import com.mymeds.medicationservice.rest.GetMedicationResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicationMapper {

  GetMedicationResponse toGetMedicationResponse(Medication medication, List<MedicationDose> medicationDoses);

  GetMedicationDose toGetMedicationDose(MedicationDose medicationDose);
}
