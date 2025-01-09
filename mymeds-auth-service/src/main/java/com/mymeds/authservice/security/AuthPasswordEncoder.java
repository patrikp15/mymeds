package com.mymeds.authservice.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthPasswordEncoder {

  private final PasswordEncoder passwordEncoder;

  public AuthPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public String encode(String password) {
    return passwordEncoder.encode(password);
  }

  public boolean isValid(String requestPassword, String password) {
    return passwordEncoder.matches(requestPassword, password);
  }
}
