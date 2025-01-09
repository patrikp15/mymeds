package com.mymeds.medicationservice.service.impl;

import com.mymeds.medicationservice.mapper.MedicationMapper;
import com.mymeds.medicationservice.persistance.dao.MedicationDoseRepository;
import com.mymeds.medicationservice.persistance.dao.MedicationRepository;
import com.mymeds.medicationservice.persistance.model.Medication;
import com.mymeds.medicationservice.persistance.model.MedicationDose;
import com.mymeds.medicationservice.rest.GetMedicationResponse;
import com.mymeds.medicationservice.service.MedicationService;
import com.mymeds.sharedutilities.exception.MedicationErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MedicationServiceImpl implements MedicationService {

  private static final Logger LOG = LoggerFactory.getLogger(MedicationServiceImpl.class);

  private final MedicationRepository medicationRepository;
  private final MedicationDoseRepository medicationDoseRepository;
  private final MedicationMapper medicationMapper;

  public MedicationServiceImpl(MedicationRepository medicationRepository,
      MedicationDoseRepository medicationDoseRepository, MedicationMapper medicationMapper) {
    this.medicationRepository = medicationRepository;
    this.medicationDoseRepository = medicationDoseRepository;
    this.medicationMapper = medicationMapper;
  }

  @Override
  public GetMedicationResponse getMedicationByMedicationId(UUID medicationId) {
    LOG.info("action=getMedicationByMedicationId, status=started, medication={}", medicationId);

    final Medication medication = medicationRepository.findOneByMedicationId(medicationId)
        .orElseThrow(() -> new MyMedsGeneralException(MedicationErrorCode.MEDICATION_NOT_FOUND));
    final List<MedicationDose> medicationDoses = medicationDoseRepository.findAllByMedicationMedicationId(medicationId);

    final GetMedicationResponse getMedicationResponse = medicationMapper.toGetMedicationResponse(
        medication, medicationDoses
    );
    LOG.info("action=getMedicationByMedicationId, status=finished");
    return getMedicationResponse;
  }

  @Override
  public List<GetMedicationResponse> getAllMedications() {
    LOG.info("action=getAllMedications, status=started");

    final List<GetMedicationResponse> response = medicationRepository.findAll().stream().map(medication -> {
      final List<MedicationDose> medicationDoses = medicationDoseRepository.findAllByMedicationMedicationId(
          medication.getMedicationId()
      );
      return medicationMapper.toGetMedicationResponse(medication, medicationDoses);
    }).toList();

    LOG.info("action=getAllMedications, status=finished");
    return response;
  }
}
