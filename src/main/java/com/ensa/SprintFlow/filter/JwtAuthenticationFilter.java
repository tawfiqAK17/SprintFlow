package com.ensa.SprintFlow.filter;

import com.ensa.SprintFlow.exception.generalException.ResourceExpiredException;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import com.ensa.SprintFlow.model.security.UserContext;
import com.ensa.SprintFlow.service.security.JwtService;
import com.ensa.SprintFlow.service.security.UserContextService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  JwtService jwtService;
  UserContextService userContextService;

  public JwtAuthenticationFilter(JwtService jwtService, UserContextService userContextService) {
    this.jwtService = jwtService;
    this.userContextService = userContextService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwt = getJwt(request);
    if (jwt == null) {
      filterChain.doFilter(request, response);
      return;
    }
    String username;
    try {
      username = jwtService.extractUsername(jwt);
    } catch (ExpiredJwtException e) {
      throw new ResourceExpiredException("the jwt was expired");
    } catch (Exception e) {
      throw new UnauthorizedException("the jwt is not valid");
    }

    UserContext userContext = userContextService.loadUserByUsername(username);

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userContext, null, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    filterChain.doFilter(request, response);
  }

  private String getJwt(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null) {
      if (authorizationHeader.startsWith("Bearer ")) {
        return authorizationHeader.substring(7);
      }
    }
    return null;
  }
}
