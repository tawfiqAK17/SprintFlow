package com.ensa.SprintFlow.security.filter;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.ResourceHierarchyValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class ResourceHierarchyValidationFilter extends OncePerRequestFilter {
  ResourceHierarchyValidator validator;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String url = request.getRequestURI();
    String queryParams = request.getQueryString();
    if (queryParams != null) {
      url += "?" + queryParams;
    }
    validator.validate(url);

    filterChain.doFilter(request, response);
  }
}
