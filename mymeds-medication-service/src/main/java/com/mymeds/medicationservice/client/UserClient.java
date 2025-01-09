package com.mymeds.medicationservice.client;

import com.mymeds.medicationservice.client.rest.UserClientResponse;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient extends BaseClient {

  public UserClient(
      RestTemplate restTemplate,
      @Value("${mymeds.url.user-service-base-url}") String baseUrl
  ) {
    super(restTemplate, baseUrl);
  }

  public UserClientResponse getUserDetail(UUID userId) {
    return requestGet(
        "/users/" + userId.toString(),
        UserClientResponse.class
    );
  }
}
