package com.ensa.SprintFlow.config;

import com.ensa.SprintFlow.filter.FilerExceptionHandler;
import com.ensa.SprintFlow.security.filter.JwtAuthenticationFilter;
import com.ensa.SprintFlow.security.filter.ResourceHierarchyValidationFilter;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SecurityConfiguration {

  JwtAuthenticationFilter jwtAuthenticationFilter;
  ResourceHierarchyValidationFilter resourceHierarchyValidationFilter;
  FilerExceptionHandler filterExceptionHandler;

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
                    .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html"
                    ).permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(filterExceptionHandler, JwtAuthenticationFilter.class)
        .addFilterAfter(resourceHierarchyValidationFilter, JwtAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
