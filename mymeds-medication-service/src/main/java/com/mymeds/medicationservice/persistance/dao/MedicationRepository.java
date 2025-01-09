package com.mymeds.medicationservice.persistance.dao;

import com.mymeds.medicationservice.persistance.model.Medication;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

  Optional<Medication> findOneByMedicationId(UUID medicationId);
}
