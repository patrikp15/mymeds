package com.mymeds.authservice.client;

import com.mymeds.authservice.client.rest.notification.AuthNotificationRequest;
import com.mymeds.authservice.mapper.NotificationClientMapper;
import com.mymeds.authservice.persistance.model.AuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClient extends BaseClient {

  private final NotificationClientMapper notificationClientMapper;

  public NotificationClient(
      RestTemplate restTemplate,
      @Value("${mymeds.url.notification-service-base-url}") String baseUrl,
      NotificationClientMapper notificationClientMapper
  ) {
    super(restTemplate, baseUrl);
    this.notificationClientMapper = notificationClientMapper;
  }

  public void sendAuthNotification(String email, AuthToken authToken) {
    final AuthNotificationRequest authNotificationRequest = notificationClientMapper.toAuthNotificationRequest(
        email,
        authToken
    );
    requestPost(
        authNotificationRequest,
        "/auth",
        Void.class
    );
  }
}
