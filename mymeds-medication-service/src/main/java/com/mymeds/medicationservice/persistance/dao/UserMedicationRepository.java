package com.mymeds.medicationservice.persistance.dao;

import com.mymeds.medicationservice.persistance.model.UserMedication;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMedicationRepository extends JpaRepository<UserMedication, Long> {

  Optional<UserMedication> findByUserMedicationId(UUID userMedicationId);

  List<UserMedication> findAllByUserId(UUID userId);

  @Query("SELECT um FROM UserMedication um WHERE :currentDate BETWEEN um.startDate AND um.endDate")
  List<UserMedication> findAllByCurrentDateWithinRange(@Param("currentDate") LocalDate currentDate);

  @Query("SELECT um FROM UserMedication um WHERE um.endDate < :currentDate")
  List<UserMedication> findAllPastMedications(@Param("currentDate") LocalDate currentDate);

  @Query("SELECT um FROM UserMedication um WHERE um.startDate > :currentDate")
  List<UserMedication> findAllFutureMedications(@Param("currentDate") LocalDate currentDate);

  @Query("SELECT um FROM UserMedication um WHERE LOWER(um.medication.name) LIKE LOWER(CONCAT(:textFilter, '%'))")
  List<UserMedication> findByMedicationNameStartingWith(@Param("textFilter") String textFilter);
}
