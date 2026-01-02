package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ValidateEpicInProjectStrategyTest {
  @Autowired ValidateEpicInProjectStrategy validateEpicInProjectStrategy;

  @Test
  public void suportedUrlTest() {
    String url = "/projects/1/epics/1";
    assertEquals(true, validateEpicInProjectStrategy.supports(url));
  }

  @Test
  public void unsuportedUrlTest() {
    String url = "/projects/1/epics/1/user_stories";
    assertEquals(false, validateEpicInProjectStrategy.supports(url));
  }
}
