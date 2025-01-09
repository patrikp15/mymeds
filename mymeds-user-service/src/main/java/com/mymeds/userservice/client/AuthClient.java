package com.mymeds.userservice.client;

import com.mymeds.userservice.client.rest.AuthUserDetailClientResponse;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthClient extends BaseClient {

  public AuthClient(
      RestTemplate restTemplate,
      @Value("${mymeds.url.auth-service}") String baseUrl
  ) {
    super(restTemplate, baseUrl);
  }

  public AuthUserDetailClientResponse getUserDetailById(UUID userId) {
    return requestGet("/auth/users/" + userId, AuthUserDetailClientResponse.class);
  }
}
