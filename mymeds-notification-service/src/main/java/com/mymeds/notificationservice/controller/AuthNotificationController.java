package com.mymeds.notificationservice.controller;

import com.mymeds.notificationservice.rest.auth.AuthNotificationRequest;
import com.mymeds.notificationservice.service.AuthNotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthNotificationController {

  private final AuthNotificationService authNotificationService;

  public AuthNotificationController(AuthNotificationService authNotificationService) {
    this.authNotificationService = authNotificationService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void sendAuthNotification(@RequestBody @Valid AuthNotificationRequest request) {
    authNotificationService.sendAuthNotification(request);
  }
}
