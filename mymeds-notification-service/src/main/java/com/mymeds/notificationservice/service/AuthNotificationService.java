package com.mymeds.notificationservice.service;

import com.mymeds.notificationservice.rest.auth.AuthNotificationRequest;

public interface AuthNotificationService {

  void sendAuthNotification(AuthNotificationRequest request);
}
