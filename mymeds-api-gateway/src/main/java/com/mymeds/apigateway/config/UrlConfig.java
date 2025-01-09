package com.mymeds.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mymeds.url")
public class UrlConfig {

  public static final String AUTH_SERVICE_VERSION_PATH = "/api/v1";
  public static final String USER_SERVICE_VERSION_PATH = "/api/v1";
  public static final String MEDICATION_SERVICE_VERSION_PATH = "/api/v1";

  private String authServiceBaseUrl;
  private String userServiceBaseUrl;
  private String medicationServiceBaseUrl;
  private String feBaseUrl;

  public String getAuthServiceBaseUrl() {
    return authServiceBaseUrl;
  }

  public void setAuthServiceBaseUrl(String authServiceBaseUrl) {
    this.authServiceBaseUrl = authServiceBaseUrl;
  }

  public String getUserServiceBaseUrl() {
    return userServiceBaseUrl;
  }

  public void setUserServiceBaseUrl(String userServiceBaseUrl) {
    this.userServiceBaseUrl = userServiceBaseUrl;
  }

  public String getMedicationServiceBaseUrl() {
    return medicationServiceBaseUrl;
  }

  public void setMedicationServiceBaseUrl(String medicationServiceBaseUrl) {
    this.medicationServiceBaseUrl = medicationServiceBaseUrl;
  }

  public String getFeBaseUrl() {
    return feBaseUrl;
  }

  public void setFeBaseUrl(String feBaseUrl) {
    this.feBaseUrl = feBaseUrl;
  }
}
