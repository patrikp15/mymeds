package com.mymeds.authservice.security;

import com.mymeds.authservice.persistance.model.UserDetail;
import com.mymeds.authservice.service.ConfigService;
import com.mymeds.sharedutilities.enumeration.UserRole;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import com.mymeds.sharedutilities.exception.AuthErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtAuth {

  private static final Logger LOG = LoggerFactory.getLogger(JwtAuth.class);

  private final SecretKey secretKey;
  private final ConfigService configService;

  public JwtAuth(SecretKey secretKey, ConfigService configService) {
    this.secretKey = secretKey;
    this.configService = configService;
  }

  public String generateAccessToken(UUID userId, UserRole userRole, UserStatus userStatus) {
    return generateJWTToken(userId, userRole, userStatus, configService.getJwtAccessExpirationInMillis());
  }

  public String generateRefreshToken(UUID userId, UserRole userRole, UserStatus userStatus) {
    return generateJWTToken(userId, userRole, userStatus, configService.getJwtRefreshExpirationInMillis());
  }

  public String generateResetPasswordToken(UUID userId, UserRole userRole, UserStatus userStatus) {
    return generateJWTToken(userId, userRole, userStatus, configService.getJwtResetPasswordExpirationInMillis());
  }

  public String generateGuestRegistrationToken(UUID userId, UserRole userRole, UserStatus userStatus) {
    return generateJWTToken(userId, userRole, userStatus, configService.getJwtGuestRegistrationExpirationInMillis());
  }

  private String generateJWTToken(UUID userId, UserRole userRole, UserStatus userStatus, long expiryTimeInMillis) {
    final Date issuedAt = new Date();
    final Date expiration = new Date(issuedAt.getTime() + expiryTimeInMillis);
    return Jwts.builder()
        .subject(userId.toString())
        .issuedAt(issuedAt)
        .expiration(expiration)
        .signWith(secretKey)
        .claim("userRole", userRole)
        .claim("userStatus", userStatus)
        .compact();
  }

  public Optional<String> getUserIdFromJWT(String token) {
    return Optional.ofNullable(getClaimFromToken(token, Claims::getSubject));
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T extends Enum<T>> Optional<T> getFromToken(String token, Class<T> clazz, String key) {
    try {
      final String value = getAllClaimsFromToken(token).get(key, String.class);
      return Arrays.stream(clazz.getEnumConstants())
          .filter(val -> val.name().equals(value))
          .findAny();
    } catch (Exception e) {
      LOG.error("action=getFromToken, status=failed, message=unable to cast value from key={}", key);
      return Optional.empty();
    }
  }

  public Boolean isTokenRefreshable(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return !expiration.before(new Date());
  }

  public String refreshJwtToken(String token) {
    final String subject = getClaimFromToken(token, Claims::getSubject);
    final Date currentDate = new Date();
    final Date expiryDate = new Date(currentDate.getTime() + configService.getJwtAccessExpirationInMillis());
    return Jwts.builder()
        .subject(subject)
        .issuedAt(currentDate)
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();
  }

  public void validate(String token) {
    LOG.info("action=validate, status=started");
    try {
      Jwts.parser()
          .verifyWith(secretKey).build()
          .parseSignedClaims(token);
      LOG.info("action=validate, status=finished");
    } catch (JwtException | IllegalArgumentException e) {
      LOG.error("action=validate, status=failed, message=JWT Token parsing failed", e);
      throw new MyMedsGeneralException(AuthErrorCode.AUTH_FAILED, e);
    }
  }

  private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    try {
      final Claims claims = getAllClaimsFromToken(token);
      return claimsResolver.apply(claims);
    } catch (Exception e) {
      if (e instanceof ExpiredJwtException) {
        throw new MyMedsGeneralException(AuthErrorCode.JWT_TOKEN_EXPIRED, e);
      }
      throw new MyMedsGeneralException(AuthErrorCode.AUTH_FAILED, e);
    }
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .verifyWith(secretKey).build()
        .parseSignedClaims(token)
        .getPayload();
  }

  @FunctionalInterface
  public interface TokenGenerator {
    String generate(UserDetail userDetail);
  }
}