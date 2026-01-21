package com.ensa.SprintFlow.security.aspect;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeRoles;
import com.ensa.SprintFlow.security.service.UserAuthorizationService;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class ProjectAuthorizationAspect {
  UserAuthorizationService userAuthorizationService;

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeProductOwner)")
  public ResponseEntity<?> AuthorizeProductOwner(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRoles(Role.PRODUCT_OWNER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a PRODUCT_OWNER");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeScrumMaster)")
  public ResponseEntity<?> AuthorizeScrumMaster(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRoles(Role.SCRUM_MASTER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a SCRUM_MASTER");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeDeveloper)")
  public ResponseEntity<?> AuthorizeDeveloper(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRoles(Role.DEVELOPER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a DEVELOPER");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeTester)")
  public ResponseEntity<?> AuthorizeTester(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRoles(Role.TESTER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a TESTER");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeMember)")
  public ResponseEntity<?> AuthorizeMember(ProceedingJoinPoint joinPoint) throws Throwable {
    if (isUserHasRoles(Role.PRODUCT_OWNER, Role.SCRUM_MASTER, Role.DEVELOPER, Role.TESTER)) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException("the user should be a MEMBER");
  }

  @Around(
      "@annotation(com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeRoles)")
  public ResponseEntity<?> AuthorizeRoles(ProceedingJoinPoint joinPoint) throws Throwable {
    // extract the method signature
    MethodSignature methodSignature = (MethodSignature) (joinPoint.getSignature());
    // extract the method from its signature
    Method method = methodSignature.getMethod();
    // get the annotation from the method
    AuthorizeRoles annotation = method.getAnnotation(AuthorizeRoles.class);
    if (isUserHasRoles(annotation.roles())) {
      return (ResponseEntity<?>) joinPoint.proceed();
    }
    throw new UnauthorizedException(
        "the user should be a one of: " + Arrays.toString(annotation.roles()));
  }

  private boolean isUserHasRoles(Role... roles) {
    List<Role> userRoles = userAuthorizationService.getAuthenticatedUserRoles();
    for (Role role : roles) {
      if (userRoles.contains(role)) {
        return true;
      }
    }
    return false;
  }
}
