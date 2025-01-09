package com.mymeds.medicationservice.service.impl;

import com.mymeds.medicationservice.client.AuthClient;
import com.mymeds.medicationservice.client.rest.AuthUserClientResponse;
import com.mymeds.medicationservice.enumeration.RestDateFilter;
import com.mymeds.medicationservice.mapper.UserMedicationMapper;
import com.mymeds.medicationservice.persistance.dao.MedicationDoseRepository;
import com.mymeds.medicationservice.persistance.dao.MedicationRepository;
import com.mymeds.medicationservice.persistance.dao.UserMedicationRepository;
import com.mymeds.medicationservice.persistance.model.Medication;
import com.mymeds.medicationservice.persistance.model.MedicationDose;
import com.mymeds.medicationservice.persistance.model.UserMedication;
import com.mymeds.medicationservice.rest.CreateUserMedicationRequest;
import com.mymeds.medicationservice.rest.GetUserMedicationResponse;
import com.mymeds.medicationservice.security.CurrentAuditorContextHolder;
import com.mymeds.medicationservice.service.UserMedicationService;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import com.mymeds.sharedutilities.exception.MedicationErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserMedicationServiceImpl implements UserMedicationService {

  private static final Logger LOG = LoggerFactory.getLogger(UserMedicationServiceImpl.class);

  private final UserMedicationRepository userMedicationRepository;
  private final MedicationRepository medicationRepository;
  private final MedicationDoseRepository medicationDoseRepository;
  private final UserMedicationMapper userMedicationMapper;
  private final AuthClient authClient;

  public UserMedicationServiceImpl(
      UserMedicationRepository userMedicationRepository,
      MedicationRepository medicationRepository,
      MedicationDoseRepository medicationDoseRepository,
      UserMedicationMapper userMedicationMapper,
      AuthClient authClient
  ) {
    this.userMedicationRepository = userMedicationRepository;
    this.medicationRepository = medicationRepository;
    this.medicationDoseRepository = medicationDoseRepository;
    this.userMedicationMapper = userMedicationMapper;
    this.authClient = authClient;
  }

  @Override
  public void addUserMedication(CreateUserMedicationRequest request) {
    final UUID userId = getCurrentUserIdOrThrowException();
    LOG.info("action=addUserMedication, status=started, user={}, medication={}, medicationDose={}",
        userId, request.getMedicationId(), request.getMedicationDoseId());

    guardUserActive(userId);

    final Medication medication = medicationRepository.findOneByMedicationId(request.getMedicationId())
        .orElseThrow(() -> new MyMedsGeneralException(MedicationErrorCode.MEDICATION_NOT_FOUND));
    final MedicationDose medicationDose = medicationDoseRepository.findOneByMedicationDoseId(request.getMedicationDoseId())
        .map(dose -> checkMedicationMatchMedicationDose(medication, dose))
        .orElse(null);

    userMedicationRepository.save(
        userMedicationMapper.toUserMedication(request, userId, medication, medicationDose)
    );

    LOG.info("action=addUserMedication, status=finished");
  }

  @Override
  public void deleteUserMedicationById(UUID userMedicationId) {
    LOG.info("action=deleteUserMedicationById, status=started, userMedicationId={}", userMedicationId);
    final UserMedication userMedication = userMedicationRepository.findByUserMedicationId(userMedicationId)
        .orElseThrow(() -> new MyMedsGeneralException(MedicationErrorCode.USER_MEDICATION_NOT_FOUND));
    userMedicationRepository.delete(userMedication);
    LOG.info("action=deleteUserMedicationById, status=finished}");
  }

  @Override
  public List<GetUserMedicationResponse> getUserMedications() {
    final UUID userId = getCurrentUserIdOrThrowException();
    LOG.info("action=getUserMedicationsByUserId, status=started, user={}", userId);
    final List<GetUserMedicationResponse> userMedications = userMedicationRepository.findAllByUserId(userId).stream()
        .map(userMedicationMapper::toGetUserMedication)
        .toList();
    LOG.info("action=getUserMedicationsByUserId, status=finished");
    return userMedications;
  }

  @Override
  public List<GetUserMedicationResponse> getUserMedicationsByUserId(UUID uid) {
    LOG.info("action=getUserMedicationsByUserId, status=started, userId={}", uid);
    final List<GetUserMedicationResponse> userMedications = userMedicationRepository.findAllByUserId(uid)
        .stream()
        .map(userMedicationMapper::toGetUserMedication)
        .toList();

    LOG.info("action=getUserMedicationsByUserId, status=finished");
    return userMedications;
  }

  @Override
  public List<GetUserMedicationResponse> getUserMedicationsByDateFilter(RestDateFilter dateFilter) {
    final UUID userId = getCurrentUserIdOrThrowException();
    LOG.info("action=getUserMedicationsByDateFilter, status=started, userId={}, dateFilter={}", userId, dateFilter);
    List<UserMedication> entities;
    if (RestDateFilter.ACTUAL.equals(dateFilter)) {
       entities = userMedicationRepository.findAllByCurrentDateWithinRange(LocalDate.now());
    } else if (RestDateFilter.PAST.equals(dateFilter)) {
      entities = userMedicationRepository.findAllPastMedications(LocalDate.now());
    } else  {
      entities = userMedicationRepository.findAllFutureMedications(LocalDate.now());
    }

    final List<GetUserMedicationResponse> response = entities.stream()
        .map(userMedicationMapper::toGetUserMedication)
        .toList();

    LOG.info("action=getUserMedicationsByDateFilter, status=finished");
    return response;
  }

  @Override
  public List<GetUserMedicationResponse> getUserMedicationsByTextFilter(String textFilter) {
    final UUID userId = getCurrentUserIdOrThrowException();
    LOG.info("action=getUserMedicationsByTextFilter, status=started, userId={}, textFilter={}", userId, textFilter);
    if (StringUtils.isBlank(textFilter) && textFilter.length() < 3) {
      LOG.info("action=getUserMedicationsByTextFilter, status=finished, message=text filter empty or short");
      return List.of();
    }
    final List<GetUserMedicationResponse> responses = userMedicationRepository.findByMedicationNameStartingWith(textFilter)
        .stream()
        .map(userMedicationMapper::toGetUserMedication)
        .toList();

    LOG.info("action=getUserMedicationsByTextFilter, status=finished");
    return responses;
  }

  private MedicationDose checkMedicationMatchMedicationDose(Medication medication, MedicationDose medicationDose) {
    final UUID medicationIdFromDose = medicationDose.getMedication().getMedicationId();
    final UUID medicationId = medication.getMedicationId();
    if (!medicationId.equals(medicationIdFromDose)) {
      LOG.error("action=checkMedicationMatchMedicationDose, status=failed, medication={}, medicationDose={}",
          medicationId, medicationIdFromDose);
      throw new MyMedsGeneralException(MedicationErrorCode.DOSE_NOT_BELONG_TO_MEDICATION);
    }
    return medicationDose;
  }

  private UUID getCurrentUserIdOrThrowException() {
    return CurrentAuditorContextHolder.get()
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND_IN_CONTEXT));
  }

  private void guardUserActive(UUID userId) {
    final AuthUserClientResponse userDetail = authClient.getAuthUserDetail(userId);
    if (!UserStatus.ACTIVE.equals(userDetail.getUserStatus())) {
      throw new MyMedsGeneralException(UserErrorCode.USER_MUST_BE_ACTIVE);
    }
  }
}
