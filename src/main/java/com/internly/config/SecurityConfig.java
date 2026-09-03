package com.internly.config;

import com.internly.security.JwtAuthenticationFilter;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

@Configuration
public class SecurityConfig {

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain filterChain(
    HttpSecurity http,
    JwtAuthenticationFilter jwt,
    @Value("${cors.allowed-origins}") String origins
  ) throws Exception {
    http
      .csrf(c -> c.disable())
      .cors(c -> c.configurationSource(corsConfiguration(origins)))
      .sessionManagement(s ->
        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(a ->
        a
          .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
          .permitAll()
          .requestMatchers("/api/admin/**")
          .hasRole("ADMIN")
          .requestMatchers(
            "/api/student/**",
            "/api/applications/**",
            "/api/recommendations/**",
            "/api/internships/**",
            "/api/notifications/**"
          )
          .hasRole("STUDENT")
          .anyRequest()
          .authenticated()
      )
      .exceptionHandling(e ->
        e
          .authenticationEntryPoint((request, response, exception) -> {
            response.setStatus(401);
            response.setContentType("application/json");
            response
              .getWriter()
              .write(
                "{\"status\":401,\"message\":\"Authentication required\"}"
              );
          })
          .accessDeniedHandler((request, response, exception) -> {
            response.setStatus(403);
            response.setContentType("application/json");
            response
              .getWriter()
              .write(
                "{\"status\":403,\"message\":\"You do not have permission to access this resource\"}"
              );
          })
      )
      .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  private CorsConfigurationSource corsConfiguration(String origins) {
    CorsConfiguration c = new CorsConfiguration();
    c.setAllowedOrigins(Arrays.asList(origins.split(",")));
    c.setAllowedMethods(
      Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    );
    c.setAllowedHeaders(List.of("*"));
    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", c);
    return source;
  }
}
