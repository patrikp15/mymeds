package com.mymeds.authservice.service.impl;

import com.mymeds.authservice.config.SpringProfileConfig;
import com.mymeds.authservice.config.security.JwtConfig;
import com.mymeds.authservice.service.ConfigService;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService {

  private final SpringProfileConfig springProfileConfig;
  private final JwtConfig jwtConfig;

  public ConfigServiceImpl(SpringProfileConfig springProfileConfig, JwtConfig jwtConfig) {
    this.springProfileConfig = springProfileConfig;
    this.jwtConfig = jwtConfig;
  }

  @Override
  public String getCurrentSpringProfile() {
    return springProfileConfig.getSpringActiveProfile();
  }

  @Override
  public long getJwtAccessExpirationInMillis() {
    return jwtConfig.jwtAccessExpirationInMillis();
  }

  @Override
  public long getJwtRefreshExpirationInMillis() {
    return jwtConfig.jwtRefreshExpirationInMillis();
  }

  @Override
  public long getJwtResetPasswordExpirationInMillis() {
    return jwtConfig.getJwtResetPasswordExpiration();
  }

  @Override
  public long getJwtGuestRegistrationExpirationInMillis() {
    return jwtConfig.getJwtGuestRegistrationExpiration();
  }
}
