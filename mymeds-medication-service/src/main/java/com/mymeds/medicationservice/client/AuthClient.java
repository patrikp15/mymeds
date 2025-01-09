package com.mymeds.medicationservice.client;

import com.mymeds.medicationservice.client.rest.AuthUserClientResponse;
import com.mymeds.medicationservice.client.rest.UserClientResponse;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthClient extends BaseClient {

  public AuthClient(
      RestTemplate restTemplate,
      @Value("${mymeds.url.auth-service-base-url}") String baseUrl
  ) {
    super(restTemplate, baseUrl);
  }

  public AuthUserClientResponse getAuthUserDetail(UUID userId) {
    return requestGet(
        "/auth/users/" + userId.toString(),
        AuthUserClientResponse.class
    );
  }
}
