package com.mymeds.authservice.client;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MedicationClient extends BaseClient {

  public MedicationClient(
      RestTemplate restTemplate,
      @Value("${mymeds.url.medication-service-base-url}") String baseUrl
  ) {
    super(restTemplate, baseUrl);
  }

  public void generateQRCode(UUID userId) {
    requestGet(
        "/share/qr/" + userId + "/generate",
        Void.class
    );
  }
}
