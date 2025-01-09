package com.mymeds.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringProfileConfig {

  @Value("${spring.profiles.active:default}")
  private String springActiveProfile;

  public String getSpringActiveProfile() {
    return springActiveProfile;
  }
}
