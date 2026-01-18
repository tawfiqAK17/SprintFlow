package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.UserInProjectValidator;
import com.ensa.SprintFlow.service.ProjectMemberService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class UserProjectStrategy extends ResourceHierarchyValidationStrategy {

  ProjectMemberService projectMemberService;
  UserInProjectValidator userInProjectValidator;

  public UserProjectStrategy(
      AntPathMatcher pathMatcher,
      ProjectMemberService projectMemberService,
      UserInProjectValidator userInProjectValidator) {
    super(pathMatcher);
    this.projectMemberService = projectMemberService;
    this.userInProjectValidator = userInProjectValidator;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/members/{username}/**";
  }

  @Override
  public void validate(String url) {
    Long projectId = Long.parseLong(getPathVariable(url, "projectId"));
    String username = getPathVariable(url, "username");
    userInProjectValidator.validate(projectId, username);
  }
}
