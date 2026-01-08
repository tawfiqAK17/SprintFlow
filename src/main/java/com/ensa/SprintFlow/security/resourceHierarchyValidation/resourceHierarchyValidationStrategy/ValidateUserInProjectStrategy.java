package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.ProjectMemberService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class ValidateUserInProjectStrategy
    extends ResourceHierarchyValidationStrategy<Long, String> {

  ProjectMemberService projectMemberService;

  public ValidateUserInProjectStrategy(
      AntPathMatcher pathMatcher, ProjectMemberService projectMemberService) {
    super(pathMatcher);
    this.projectMemberService = projectMemberService;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/members/{username}/**";
  }

  @Override
  public void validate(String url) {
    Long projectId = Long.parseLong(getPathVariable(url, "projectId"));
    String username = getPathVariable(url, "username");
    validate(projectId, username);
  }

  @Override
  public void validate(Long parent, String child) {
    projectMemberService.findAllByProjectIdAndUsername(parent, child).stream()
        .findAny()
        .orElseThrow(
            () -> new NotFoundException("the project has no member with the given username"));
  }
}
