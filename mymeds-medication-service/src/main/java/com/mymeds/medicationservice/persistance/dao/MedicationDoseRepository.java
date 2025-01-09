package com.mymeds.medicationservice.persistance.dao;

import com.mymeds.medicationservice.persistance.model.MedicationDose;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationDoseRepository extends JpaRepository<MedicationDose, Long> {

  Optional<MedicationDose> findOneByMedicationDoseId(UUID medicationDoseId);

  List<MedicationDose> findAllByMedicationMedicationId(UUID medicationId);
}
