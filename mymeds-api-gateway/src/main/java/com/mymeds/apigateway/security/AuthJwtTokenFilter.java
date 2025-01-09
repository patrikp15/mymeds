package com.mymeds.apigateway.security;

import com.mymeds.apigateway.config.UrlConfig;
import com.mymeds.apigateway.rest.ValidateTokenResponse;
import com.mymeds.apigateway.service.AuthService;
import com.mymeds.sharedutilities.exception.AuthErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class AuthJwtTokenFilter extends OncePerRequestFilter {

  private static final String AUTH_HEADER_NAME = "Authorization";

  private final AuthService authService;
  private final HandlerExceptionResolver exceptionResolver;

  public AuthJwtTokenFilter(AuthService authService, HandlerExceptionResolver exceptionResolver) {
    this.authService = authService;
    this.exceptionResolver = exceptionResolver;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    extractJWTFromRequest(request).ifPresentOrElse(
        token -> {
          try {
            final ValidateTokenResponse validateTokenResponse = authService.validateJwtToken(token);
            setUpSecurityContext(request, validateTokenResponse);

            filterChain.doFilter(request, response);
          } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
          }
        },
        () -> exceptionResolver.resolveException(
            request,
            response,
            null,
            new MyMedsGeneralException(AuthErrorCode.AUTH_FAILED)
        )
    );
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getServletPath().contains("/public")
        || request.getServletPath().equals(UrlConfig.AUTH_SERVICE_VERSION_PATH + "/auth/register")
        || request.getServletPath().equals(UrlConfig.AUTH_SERVICE_VERSION_PATH + "/auth/login")
        || request.getServletPath().equals(UrlConfig.AUTH_SERVICE_VERSION_PATH + "/auth/password/reset")
        || request.getServletPath().equals(UrlConfig.AUTH_SERVICE_VERSION_PATH + "/auth/token")
        || request.getServletPath().equals(UrlConfig.AUTH_SERVICE_VERSION_PATH + "/auth/token/validate")
        || request.getServletPath().equals(UrlConfig.AUTH_SERVICE_VERSION_PATH + "/auth/guests/detail")
        || request.getServletPath().equals(UrlConfig.AUTH_SERVICE_VERSION_PATH + "/auth/guests/register");
  }

  private Optional<String> extractJWTFromRequest(HttpServletRequest request) {
    final Object authorization = request.getAttribute(AUTH_HEADER_NAME);
    if (authorization instanceof final String authValue) {
      if (authValue.startsWith("Bearer ")) {
        return Optional.of(authValue.substring(7));
      }
    }
    return Optional.empty();
  }

  private void setUpSecurityContext(HttpServletRequest request, ValidateTokenResponse validateTokenResponse) {
    final GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
        validateTokenResponse.getUserRole().getAuthority()
    );
    final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        validateTokenResponse,
        null,
        List.of(grantedAuthority)
    );
    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }
}
