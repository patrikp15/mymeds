package com.mymeds.notificationservice.service.impl;

import com.mymeds.notificationservice.enumeration.AuthTokenType;
import com.mymeds.notificationservice.rest.auth.AuthNotificationRequest;
import com.mymeds.notificationservice.service.AuthNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthNotificationServiceImpl implements AuthNotificationService {

  private static final Logger LOG = LoggerFactory.getLogger(AuthNotificationServiceImpl.class);

  private final String apiGatewayBaseUrl;
  private final String feBaseUrl;

  public AuthNotificationServiceImpl(
      @Value("${mymeds.url.api-gateway-base-url}") String apiGatewayBaseUrl,
      @Value("${mymeds.url.fe-base-url}") String feBaseUrl
  ) {
    this.apiGatewayBaseUrl = apiGatewayBaseUrl;
    this.feBaseUrl = feBaseUrl;
  }

  @Override
  public void sendAuthNotification(AuthNotificationRequest request) {
    LOG.info("action=sendAuthNotification, status=started, tokenType={}, tokenExpiration={}",
        request.getTokenType(), request.getTokenExpiration());
    // TODO mask email
    LOG.info("action=sendAuthNotification, status=started, email={}", request.getEmail());

    if (AuthTokenType.USER_VERIFICATION.equals(request.getTokenType())) {
      // TODO send email notification to verify user
      LOG.info("action=sendAuthNotification, url={}", composeVerifyUserUrl(request.getToken()));
    } else if (AuthTokenType.PASSWORD_RESET.equals(request.getTokenType())) {
      // TODO send email notification to reset user password user
      LOG.info("action=sendAuthNotification, url={}", composeResetUserPasswordUrl(request.getToken()));
    } else if (AuthTokenType.GUEST_REGISTRATION.equals(request.getTokenType())) {
      // TODO send email notification to register guest
      LOG.info("action=sendAuthNotification, url={}", composeGuestRegistrationUrl(request.getToken()));
    }

    LOG.info("action=sendAuthNotification, status=finished");
  }

  private String composeVerifyUserUrl(String token) {
    return apiGatewayBaseUrl + "/api/v1/auth/token?value=" + token;
  }

  private String composeResetUserPasswordUrl(String token) {
    return feBaseUrl + "/forgotten-password?token=" + token;
  }

  private String composeGuestRegistrationUrl(String token) {
    return feBaseUrl + "/guest/register?token=" + token;
  }
}
