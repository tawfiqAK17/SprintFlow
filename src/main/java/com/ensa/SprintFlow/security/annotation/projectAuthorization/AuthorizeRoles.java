package com.ensa.SprintFlow.security.annotation.projectAuthorization;

import com.ensa.SprintFlow.enums.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthorizeRoles {
  Role[] roles() default {};
}
