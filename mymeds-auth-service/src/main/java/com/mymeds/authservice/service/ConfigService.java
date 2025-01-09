package com.mymeds.authservice.service;

public interface ConfigService {

  String getCurrentSpringProfile();

  long getJwtAccessExpirationInMillis();

  long getJwtRefreshExpirationInMillis();

  long getJwtResetPasswordExpirationInMillis();

  long getJwtGuestRegistrationExpirationInMillis();
}
