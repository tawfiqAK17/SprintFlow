package com.ensa.SprintFlow.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.ensa.SprintFlow.security.model.UserContext;

public class Utils {
  public static UserContext getUserContext() {
    return (UserContext) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }  
}
