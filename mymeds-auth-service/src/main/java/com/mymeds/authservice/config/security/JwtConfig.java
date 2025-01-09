package com.mymeds.authservice.config.security;

import io.jsonwebtoken.Jwts.SIG;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

  @Value("${jwt.expiration}")
  private long jwtExpiration;

  @Value("${jwt.refresh-expiration}")
  private long jwtRefreshExpiration;

  @Value("${jwt.reset-password-expiration}")
  private long jwtResetPasswordExpiration;

  @Value("${jwt.guest-registration-expiration}")
  private long jwtGuestRegistrationExpiration;

  @Bean
  public SecretKey secretKey() {
    return SIG.HS256.key().build();
  }

  public long jwtAccessExpirationInMillis() {
    return jwtExpiration;
  }

  public long jwtRefreshExpirationInMillis() {
    return jwtRefreshExpiration;
  }

  public long getJwtResetPasswordExpiration() {
    return jwtResetPasswordExpiration;
  }

  public long getJwtGuestRegistrationExpiration() {
    return jwtGuestRegistrationExpiration;
  }
}
