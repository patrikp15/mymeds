package com.mymeds.apigateway.config;

import static com.mymeds.apigateway.config.UrlConfig.AUTH_SERVICE_VERSION_PATH;
import static com.mymeds.apigateway.config.UrlConfig.MEDICATION_SERVICE_VERSION_PATH;
import static com.mymeds.apigateway.config.UrlConfig.USER_SERVICE_VERSION_PATH;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import com.mymeds.apigateway.rest.ValidateTokenResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

  private final UrlConfig urlConfig;

  public GatewayConfig(UrlConfig urlConfig) {
    this.urlConfig = urlConfig;
  }

  @Bean
  public RouterFunction<ServerResponse> getAuthRoute() {
    return route("auth-service")
        .POST(AUTH_SERVICE_VERSION_PATH + "/auth/register", http(urlConfig.getAuthServiceBaseUrl()))
        .POST(AUTH_SERVICE_VERSION_PATH + "/auth/login", http(urlConfig.getAuthServiceBaseUrl()))
        .POST(AUTH_SERVICE_VERSION_PATH + "/auth/logout", http(urlConfig.getAuthServiceBaseUrl()))
        .POST(AUTH_SERVICE_VERSION_PATH + "/auth/password/reset", http(urlConfig.getAuthServiceBaseUrl()))
        .GET(AUTH_SERVICE_VERSION_PATH + "/auth/token", http(urlConfig.getAuthServiceBaseUrl()))
        .GET(AUTH_SERVICE_VERSION_PATH + "/auth/verify", http(urlConfig.getAuthServiceBaseUrl()))
        .GET(AUTH_SERVICE_VERSION_PATH + "/auth/{guestId}/verify", http(urlConfig.getAuthServiceBaseUrl()))
        .PUT(AUTH_SERVICE_VERSION_PATH + "/auth/users", http(urlConfig.getAuthServiceBaseUrl()))
        .PUT(AUTH_SERVICE_VERSION_PATH + "/auth/users/status", http(urlConfig.getAuthServiceBaseUrl()))
        .PUT(AUTH_SERVICE_VERSION_PATH + "/auth/guests/{guestId}", http(urlConfig.getAuthServiceBaseUrl()))
        .PUT(AUTH_SERVICE_VERSION_PATH + "/auth/guests/{guestId}/status", http(urlConfig.getAuthServiceBaseUrl()))
        .POST(AUTH_SERVICE_VERSION_PATH + "/auth/guests", http(urlConfig.getAuthServiceBaseUrl()))
        .GET(AUTH_SERVICE_VERSION_PATH + "/auth/guests/detail", http(urlConfig.getAuthServiceBaseUrl()))
        .POST(AUTH_SERVICE_VERSION_PATH + "/auth/guests/register", http(urlConfig.getAuthServiceBaseUrl()))
        .before(serverRequest -> ServerRequest.from(serverRequest)
            .header(
                "x-user-key",
                extractValidateTokenResponseFromContext(ValidateTokenResponse::getUserId)
                    .map(UUID::toString)
                    .orElse(StringUtils.EMPTY)
            )
            .build())
        .build();
  }

  @Bean
  public RouterFunction<ServerResponse> getUserRoute() {
    return route("user-service")
        .GET(USER_SERVICE_VERSION_PATH + "/users", http(urlConfig.getUserServiceBaseUrl()))
        .GET(USER_SERVICE_VERSION_PATH + "/guests", http(urlConfig.getUserServiceBaseUrl()))
        .before(serverRequest -> ServerRequest.from(serverRequest)
            .header(
                "x-user-key",
                extractValidateTokenResponseFromContext(ValidateTokenResponse::getUserId)
                    .map(UUID::toString)
                    .orElse(StringUtils.EMPTY)
            )
            .build())
        .build();
  }

  @Bean
  public RouterFunction<ServerResponse> getMedicationRoute() {
    return route("medication-service")
        .GET(MEDICATION_SERVICE_VERSION_PATH + "/public/**", http(urlConfig.getMedicationServiceBaseUrl()))
        .GET(MEDICATION_SERVICE_VERSION_PATH + "/medications", http(urlConfig.getMedicationServiceBaseUrl()))
        .GET(MEDICATION_SERVICE_VERSION_PATH + "/share/qr", http(urlConfig.getMedicationServiceBaseUrl()))
        .GET(MEDICATION_SERVICE_VERSION_PATH + "/share/pdf", http(urlConfig.getMedicationServiceBaseUrl()))
        .GET(MEDICATION_SERVICE_VERSION_PATH + "/user-medications", http(urlConfig.getMedicationServiceBaseUrl()))
        .POST(MEDICATION_SERVICE_VERSION_PATH + "/user-medications", http(urlConfig.getMedicationServiceBaseUrl()))
        .DELETE(MEDICATION_SERVICE_VERSION_PATH + "/user-medications/{id}", http(urlConfig.getMedicationServiceBaseUrl()))
        .before(serverRequest -> ServerRequest.from(serverRequest)
            .header(
                "x-user-key",
                extractValidateTokenResponseFromContext(ValidateTokenResponse::getUserId)
                    .map(UUID::toString)
                    .orElse(StringUtils.EMPTY)
            )
            .build())
        .build();
  }

  private <T> Optional<T> extractValidateTokenResponseFromContext(
      Function<ValidateTokenResponse, T> extractFieldFunction
  ) {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .filter(ValidateTokenResponse.class::isInstance)
        .map(ValidateTokenResponse.class::cast)
        .map(extractFieldFunction);
  }
}
