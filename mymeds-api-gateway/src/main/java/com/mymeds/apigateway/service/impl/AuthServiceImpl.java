package com.mymeds.apigateway.service.impl;

import com.mymeds.apigateway.config.UrlConfig;
import com.mymeds.apigateway.rest.ValidateTokenResponse;
import com.mymeds.apigateway.service.AuthService;
import com.mymeds.sharedutilities.exception.AuthErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceImpl implements AuthService {

  private static final Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

  private final RestTemplate restTemplate;
  private final UrlConfig urlConfig;

  public AuthServiceImpl(RestTemplate restTemplate, UrlConfig urlConfig) {
    this.restTemplate = restTemplate;
    this.urlConfig = urlConfig;
  }

  @Override
  public ValidateTokenResponse validateJwtToken(String token) {
    LOG.info("action=validateJwtToken, status=started");
    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    final HttpEntity<String> entity = new HttpEntity<>(headers);
    final ResponseEntity<ValidateTokenResponse> response = restTemplate.exchange(
        getTokenValidationUrl(),
        HttpMethod.GET,
        entity,
        ValidateTokenResponse.class
    );
    final ValidateTokenResponse tokenResponse = validateStatusAndReturnBody(response);
    LOG.info("action=validateJwtToken, status=finished");
    return tokenResponse;
  }

  private ValidateTokenResponse validateStatusAndReturnBody(ResponseEntity<ValidateTokenResponse> response) {
    if (response.getStatusCode().isError()) {
      LOG.error("action=validateStatusAndReturnBody, status=failed, internalMessage=Token could not be validated, status={}",
          response.getStatusCode().value());
      throw new MyMedsGeneralException(AuthErrorCode.AUTH_FAILED);
    }
    LOG.info("action=validateStatusAndReturnBody, status=finished, internalMessage=Token successfully validated");
    return response.getBody();
  }

  private String getTokenValidationUrl() {
    return urlConfig.getAuthServiceBaseUrl() + UrlConfig.AUTH_SERVICE_VERSION_PATH + "/auth/token/validate";
  }
}
