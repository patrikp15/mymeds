package com.mymeds.apigateway.config;

import static com.mymeds.apigateway.config.UrlConfig.AUTH_SERVICE_VERSION_PATH;
import static com.mymeds.apigateway.config.UrlConfig.MEDICATION_SERVICE_VERSION_PATH;
import static com.mymeds.apigateway.config.UrlConfig.USER_SERVICE_VERSION_PATH;

import com.mymeds.apigateway.security.AuthCookieToHeaderFilter;
import com.mymeds.apigateway.security.AuthEntryPoint;
import com.mymeds.apigateway.security.AuthJwtTokenFilter;
import com.mymeds.apigateway.service.AuthService;
import com.mymeds.sharedutilities.enumeration.UserRole;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthEntryPoint authEntryPoint;
  private final AuthService authService;
  private final HandlerExceptionResolver handlerExceptionResolver;
  private final UrlConfig urlConfig;

  public SecurityConfig(
      AuthEntryPoint authEntryPoint,
      AuthService authService,
      HandlerExceptionResolver handlerExceptionResolver,
      UrlConfig urlConfig) {
    this.authEntryPoint = authEntryPoint;
    this.authService = authService;
    this.handlerExceptionResolver = handlerExceptionResolver;
    this.urlConfig = urlConfig;
  }

  @Bean
  public AuthJwtTokenFilter authJwtTokenFilter() {
    return new AuthJwtTokenFilter(authService, handlerExceptionResolver);
  }

  @Bean
  public AuthCookieToHeaderFilter authCookieToHeaderFilter() {
    return new AuthCookieToHeaderFilter();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(conf -> conf.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            // Auth Service
            .requestMatchers(HttpMethod.POST,AUTH_SERVICE_VERSION_PATH + "/auth/register").permitAll()
            .requestMatchers(HttpMethod.POST,AUTH_SERVICE_VERSION_PATH + "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST,AUTH_SERVICE_VERSION_PATH + "/auth/logout").hasAnyRole(UserRole.BASIC.name(), UserRole.GUEST.name())
            .requestMatchers(HttpMethod.POST,AUTH_SERVICE_VERSION_PATH + "/auth/password/reset").permitAll()
            .requestMatchers(HttpMethod.GET, AUTH_SERVICE_VERSION_PATH + "/auth/token").permitAll()
            .requestMatchers(HttpMethod.GET, AUTH_SERVICE_VERSION_PATH + "/auth/verify").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.GET, AUTH_SERVICE_VERSION_PATH + "/auth/{guestId}/verify").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.PUT, AUTH_SERVICE_VERSION_PATH + "/auth/users").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.PUT, AUTH_SERVICE_VERSION_PATH + "/auth/users/status").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.PUT, AUTH_SERVICE_VERSION_PATH + "/auth/guests/{guestId}").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.PUT, AUTH_SERVICE_VERSION_PATH + "/auth/guests/{guestId}/status").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.GET, AUTH_SERVICE_VERSION_PATH + "/auth/guests/detail").permitAll()
            .requestMatchers(HttpMethod.POST, AUTH_SERVICE_VERSION_PATH + "/auth/guests/register").permitAll()
            .requestMatchers(HttpMethod.POST, AUTH_SERVICE_VERSION_PATH + "/auth/guests").hasRole(UserRole.BASIC.name())
            // User Service
            .requestMatchers(HttpMethod.GET, USER_SERVICE_VERSION_PATH + "/guests").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.GET, USER_SERVICE_VERSION_PATH + "/users").hasRole(UserRole.BASIC.name())
            // Medication Service
            .requestMatchers(HttpMethod.GET, MEDICATION_SERVICE_VERSION_PATH + "/medications").hasAnyRole(UserRole.BASIC.name(), UserRole.GUEST.name())
            .requestMatchers(HttpMethod.GET, MEDICATION_SERVICE_VERSION_PATH + "/share/qr").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.GET, MEDICATION_SERVICE_VERSION_PATH + "/share/pdf").hasRole(UserRole.BASIC.name())
            .requestMatchers(HttpMethod.POST,MEDICATION_SERVICE_VERSION_PATH + "/user-medications").hasAnyRole(UserRole.BASIC.name(), UserRole.GUEST.name())
            .requestMatchers(HttpMethod.GET,MEDICATION_SERVICE_VERSION_PATH + "/user-medications").hasAnyRole(UserRole.BASIC.name(), UserRole.GUEST.name())
            .requestMatchers(HttpMethod.DELETE,MEDICATION_SERVICE_VERSION_PATH + "/user-medications/{id}").hasAnyRole(UserRole.BASIC.name(), UserRole.GUEST.name())
            .requestMatchers(MEDICATION_SERVICE_VERSION_PATH + "/public/**").permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
        .addFilterBefore(authCookieToHeaderFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
        List.of(
            urlConfig.getFeBaseUrl()
        )
    );
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
    configuration.setAllowCredentials(true);
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Disable default UserDetailsServiceAutoConfiguration
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager();
  }
}