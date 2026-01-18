package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import static org.junit.jupiter.api.Assertions.*;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.AntPathMatcher;

@ExtendWith(MockitoExtension.class)
class UserStoryEpicProjectStrategyTest {

  private UserStoryEpicProjectStrategy strategy;
  private AntPathMatcher pathMatcher;

  @Mock private EpicInProjectValidator epicInProjectValidator;
  @Mock private UserStoryInEpicValidator userStoryInEpicValidator;

  @BeforeEach
  void setUp() {
    pathMatcher = new AntPathMatcher();
    strategy =
        new UserStoryEpicProjectStrategy(
            pathMatcher, epicInProjectValidator, userStoryInEpicValidator);
  }

  @Test
  void supportedUrlTest() {
    // Test valid URLs with epic_id query param
    assertTrue(strategy.supports("/projects/1/user_stories/2?epic_id=3"));
    assertTrue(strategy.supports("/projects/100/user_stories/200?epic_id=300"));
  }

  @Test
  void unsupportedUrlTest() {
    // Test invalid URLs
    assertFalse(strategy.supports("/projects/1"));
    assertFalse(strategy.supports("/invalid/path"));
    assertFalse(strategy.supports("/projects/abc/invalid"));

    // Test missing query params
    assertFalse(strategy.supports("/projects/1/user_stories/2"));

    // Test wrong query params
    assertFalse(strategy.supports("/projects/1/user_stories/2?sprint_id=3"));
    assertFalse(strategy.supports("/projects/1/user_stories/2?epic_id=3&sprint_id=4"));
  }
}
