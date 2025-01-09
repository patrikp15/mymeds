package com.mymeds.medicationservice.config;

import com.mymeds.medicationservice.enumeration.MedicationRoute;
import com.mymeds.medicationservice.persistance.dao.MedicationDoseRepository;
import com.mymeds.medicationservice.persistance.dao.MedicationRepository;
import com.mymeds.medicationservice.persistance.model.Medication;
import com.mymeds.medicationservice.persistance.model.MedicationDose;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MedicationConfig {

  private static final Logger LOG = LoggerFactory.getLogger(MedicationConfig.class);

  private final MedicationRepository medicationRepository;
  private final MedicationDoseRepository medicationDoseRepository;

  public MedicationConfig(MedicationRepository medicationRepository, MedicationDoseRepository medicationDoseRepository) {
    this.medicationRepository = medicationRepository;
    this.medicationDoseRepository = medicationDoseRepository;

    if (medicationRepository.count() < 1) {
      readAndSaveMedication();
    } else {
      LOG.info("Medications already saved. File processing skipped.");
    }
  }

  private void readAndSaveMedication() {
    LOG.info("action=readAndSaveMedication, status=started");
    final String file = "static/medication.csv";
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file)) {
      if (inputStream != null) {
        final InputStreamReader reader = new InputStreamReader(inputStream);
        final CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

        csvParser.stream().skip(1).forEach(record -> {
          final String name = record.get(0);
          final String description = record.get(1);
          final String route = record.get(2);
          final Medication medication = new Medication();
          medication.setName(name);
          medication.setDescription(description);
          medication.setRoute(MedicationRoute.getByName(route).orElse(MedicationRoute.OTHER));
          medicationRepository.save(medication);

          final String dose = record.get(3);
          final String frequency = record.get(4);
          final String doseDescription = record.get(5);
          final MedicationDose medicationDose = new MedicationDose();
          medicationDose.setMedication(medication);
          medicationDose.setDose(dose);
          medicationDose.setFrequency(frequency);
          medicationDose.setDescription(doseDescription);
          medicationDoseRepository.save(medicationDose);
        });
      }
    } catch (IOException e) {
      LOG.error("action=readAndSaveMedication, status=failed, message=unable to read resource file={}", file, e);
    }
    LOG.info("action=readAndSaveMedication, status=finished");
  }
}
