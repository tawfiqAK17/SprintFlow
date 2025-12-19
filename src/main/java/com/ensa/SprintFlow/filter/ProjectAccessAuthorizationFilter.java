package com.ensa.SprintFlow.filter;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import com.ensa.SprintFlow.model.security.UserContext;
import com.ensa.SprintFlow.service.security.UserAuthorizationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class ProjectAccessAuthorizationFilter extends OncePerRequestFilter {

  private AntPathMatcher pathMatcher;
  private UserAuthorizationService userAuthorizationService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String uri = request.getRequestURI();
    String pattern = "/projects/{id}/**";

    if (!pathMatcher.match(pattern, uri)) {
      filterChain.doFilter(request, response);
    }

    Map<String, String> pathVariables = pathMatcher.extractUriTemplateVariables(pattern, uri);
    Long projectId = Long.parseLong(pathVariables.get("id"));

    UserContext userContext =
        (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    userContext.setRoles(userAuthorizationService.getRoles(projectId, userContext.getId()));
    if (userContext.getRoles().isEmpty()) {
      throw new UnauthorizedException("the user should be a member of the project");
    }
    if (userContext.getRoles().contains(Role.PRODUCT_OWNER))
      filterChain.doFilter(request, response);
  }
}
