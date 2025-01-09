package com.mymeds.authservice.controller;

import com.mymeds.authservice.enumeration.AuthTokenStep;
import com.mymeds.authservice.rest.AuthUserResponse;
import com.mymeds.authservice.rest.LoginUserRequest;
import com.mymeds.authservice.rest.PasswordResetRequest;
import com.mymeds.authservice.rest.RegisterUserRequest;
import com.mymeds.authservice.rest.ValidateTokenResponse;
import com.mymeds.authservice.security.CurrentAuditorContextHolder;
import com.mymeds.authservice.security.JwtAuth;
import com.mymeds.authservice.service.AuthService;
import com.mymeds.authservice.service.AuthTokenService;
import com.mymeds.authservice.service.ConfigService;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtAuth jwtAuth;
  private final ConfigService configService;
  private final AuthTokenService authTokenService;

  public AuthController(
      AuthService authService,
      JwtAuth jwtAuth,
      ConfigService configService,
      AuthTokenService authTokenService
  ) {
    this.authService = authService;
    this.jwtAuth = jwtAuth;
    this.configService = configService;
    this.authTokenService = authTokenService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthUserResponse> register(@RequestBody @Valid RegisterUserRequest request) {
    final AuthUserResponse response = authService.registerUser(request);

    return buildAuthUserResponseEntityWithAuthCookies(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthUserResponse> login(@RequestBody @Valid LoginUserRequest request) {
    final AuthUserResponse response = authService.loginUser(request);

    return buildAuthUserResponseEntityWithAuthCookies(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<AuthUserResponse> logout() {
    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            createAccessTokenCookie(null, 0).toString()
        )
        .header(
            HttpHeaders.SET_COOKIE,
            createRefreshTokenCookie(null, 0).toString()
        )
        .build();
  }

  @GetMapping("/token/validate")
  public ResponseEntity<ValidateTokenResponse> validate(@RequestHeader("Authorization") String authorization) {
    return extractJWTFromRequest(authorization)
        .map(authService::validateJwtToken)
        .map(ResponseEntity::ok)
        .orElse(
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidateTokenResponse.builder().build())
        );
  }

  @PostMapping("/password/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resetPassword(@RequestBody @Valid PasswordResetRequest request) {
    authTokenService.resetPassword(request);
  }

  @GetMapping("/token")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyUser(@RequestParam(name = "value") String value) {
    authTokenService.verifyUser(value, AuthTokenStep.VERIFICATION_STEP, null);
  }

  @GetMapping("/{guestId}/verify")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyGuest(@PathVariable(name = "guestId") UUID guestId) {
    authTokenService.verifyGuest(guestId);
  }

  @GetMapping("/verify")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyUser() {
    authTokenService.verifyUser(null, AuthTokenStep.NOTIFICATION_STEP, getUserIdOrThrowException());
  }

  private Optional<String> extractJWTFromRequest(String authorization) {
    if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
      return Optional.of(authorization.substring(7));
    }
    return Optional.empty();
  }

  private ResponseCookie createAccessTokenCookie(String token, long expirationInMillis) {
    final boolean isDev = configService.getCurrentSpringProfile().equals("dev");
    return ResponseCookie.from("authToken", token)
        .httpOnly(true)
        .secure(!isDev)
        .path("/")
        .maxAge(millisToSecond(expirationInMillis))
        .sameSite(isDev ? "Lax" : "Strict")
        .build();
  }

  private ResponseCookie createRefreshTokenCookie(String token, long expirationInMillis) {
    final boolean isDev = configService.getCurrentSpringProfile().equals("dev");
    return ResponseCookie.from("refreshToken", token)
        .httpOnly(true)
        .secure(!isDev)
        .path("/")
        .maxAge(millisToSecond(expirationInMillis))
        .sameSite(isDev ? "Lax" : "Strict")
        .build();
  }

  private ResponseEntity<AuthUserResponse> buildAuthUserResponseEntityWithAuthCookies(AuthUserResponse response) {
    final String accessToken = jwtAuth.generateAccessToken(
        response.getUserId(), response.getUserRole(), response.getUserStatus()
    );
    final String refreshToken = jwtAuth.generateRefreshToken(
        response.getUserId(), response.getUserRole(), response.getUserStatus()
    );

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            createAccessTokenCookie(accessToken, configService.getJwtAccessExpirationInMillis()).toString()
        )
        .header(
            HttpHeaders.SET_COOKIE,
            createRefreshTokenCookie(refreshToken, configService.getJwtRefreshExpirationInMillis()).toString()
        )
        .body(response);
  }

  private long millisToSecond(long millis) {
    return millis < 1 ? 0 : millis / 1000;
  }

  private UUID getUserIdOrThrowException() {
    return CurrentAuditorContextHolder.get()
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));
  }
}
