package com.mymeds.apigateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymeds.sharedutilities.exception.RestErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthEntryPoint.class);

  private final ObjectMapper objectMapper;

  public AuthEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException {
    LOGGER.error("action=AuthEntryPointJwt.commence status=failed, path={}", request.getServletPath(), authException);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    final RestErrorResponse errorResponse = new RestErrorResponse(
        authException.getMessage(),
        HttpStatus.UNAUTHORIZED.value()
    );
    objectMapper.writeValue(response.getOutputStream(), errorResponse);
  }
}