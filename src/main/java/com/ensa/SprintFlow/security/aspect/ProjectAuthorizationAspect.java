package com.ensa.SprintFlow.security.aspect;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import com.ensa.SprintFlow.security.model.UserContext;
import com.ensa.SprintFlow.security.service.UserAuthorizationService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class ProjectAuthorizationAspect {
  UserAuthorizationService userAuthorizationService;

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeProductOwner)")
  public ResponseEntity<?> AuthorizeProductOwner(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRole(Role.PRODUCT_OWNER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a ProductOwner");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeScrumMaster)")
  public ResponseEntity<?> AuthorizeScrumMaster(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRole(Role.SCRUM_MASTER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a ScrumMaster");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeDeveloper)")
  public ResponseEntity<?> AuthorizeDeveloper(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRole(Role.DEVELOPER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a Developer");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeTester)")
  public ResponseEntity<?> AuthorizeTester(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRole(Role.TESTER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a Tester");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeMember)")
  public ResponseEntity<?> AuthorizeMember(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRole(Role.PRODUCT_OWNER, Role.SCRUM_MASTER, Role.DEVELOPER, Role.TESTER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a Member");
  }

  private boolean isUserHasRole(Role... roles) {
    // get the user context
    UserContext userContext =
        (UserContext) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    // get the user roles in the project
    List<Role> userRoles =
        userAuthorizationService.getRoles(userContext.getProject().getId(), userContext.getId());
    for (Role role : roles) {
      return userRoles.contains(role);
    }
    return false;
  }
}
