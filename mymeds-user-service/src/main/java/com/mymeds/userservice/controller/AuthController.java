package com.mymeds.userservice.controller;

import com.mymeds.userservice.client.rest.AuthRegisterUserClientRequest;
import com.mymeds.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PostMapping("/register")
  public void register(@RequestBody @Valid AuthRegisterUserClientRequest request) {
    authService.registerUser(request);
  }
}
