package com.ensa.SprintFlow.config;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.filter.FilerExceptionHandler;
import com.ensa.SprintFlow.filter.JwtAuthenticationFilter;
import com.ensa.SprintFlow.filter.ProjectAccessAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
  ProjectAccessAuthorizationFilter projectAccessAuthorizationFilter;
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
  public AuthorizationManager authorizationManager() {
    AuthorizationManager authorizationManager = new AuthorizationManager();
    return authorizationManager
        .forUrl("/project/{id}")
        .allow(Role.PRODUCT_OWNER) // authorize all http methods to the PRODUCT_OWNER
        .allowMethods(Role.SCRUM_MASTER, HttpMethod.GET)
        .done();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
