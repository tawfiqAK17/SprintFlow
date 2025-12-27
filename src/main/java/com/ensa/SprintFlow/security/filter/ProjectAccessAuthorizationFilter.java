package com.ensa.SprintFlow.security.filter;

import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.security.model.UserContext;
import com.ensa.SprintFlow.service.ProjectService;
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
  private ProjectService projectService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String uri = request.getRequestURI();
    String pattern = "/projects/{id}/**";

    if (!pathMatcher.match(pattern, uri)) {
      filterChain.doFilter(request, response);
      return;
    }

    Map<String, String> pathVariables = pathMatcher.extractUriTemplateVariables(pattern, uri);
    Long projectId = Long.parseLong(pathVariables.get("id"));
    Project project = projectService.findById(projectId);

    UserContext userContext =
        (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    userContext.setProject(project);
    filterChain.doFilter(request, response);
  }
}
