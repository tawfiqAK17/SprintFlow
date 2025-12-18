package com.ensa.SprintFlow.config;

import com.ensa.SprintFlow.filter.FilerExceptionHandler;
import com.ensa.SprintFlow.filter.JwtAuthenticationFilter;
import com.ensa.SprintFlow.filter.ProjectAccessAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  JwtAuthenticationFilter jwtAuthenticationFilter;
  ProjectAccessAuthorizationFilter projectAccessAuthorizationFilter;
  FilerExceptionHandler filterExceptionHandler;

  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      FilerExceptionHandler filterExceptionHandler,
      ProjectAccessAuthorizationFilter projectAccessAuthorizationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.projectAccessAuthorizationFilter = projectAccessAuthorizationFilter;
    this.filterExceptionHandler = filterExceptionHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http.csrf(crsf -> crsf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/login")
                    .permitAll()
                    .requestMatchers("/register")
                    .permitAll()
                    .requestMatchers("/verify/**")
                    .permitAll()
                    .requestMatchers("/refresh-token")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(projectAccessAuthorizationFilter, JwtAuthenticationFilter.class)
        .addFilterBefore(filterExceptionHandler, JwtAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
