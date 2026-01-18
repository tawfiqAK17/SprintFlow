package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.AntPathMatcher;

@ExtendWith(MockitoExtension.class)
class UserStorySprintProjectStrategyTest {

  private UserStorySprintProjectStrategy strategy;
  private AntPathMatcher pathMatcher;

  @Mock private UserStoryInSprintValidator userStoryInSprintValidator;
  @Mock private SprintInProjectValidator sprintInProjectValidator;

  @BeforeEach
  void setUp() {
    pathMatcher = new AntPathMatcher();
    strategy =
        new UserStorySprintProjectStrategy(
            pathMatcher, userStoryInSprintValidator, sprintInProjectValidator);
  }

  @Test
  void supportedUrlTest() {
    // Test valid URLs
    assertTrue(strategy.supports("/projects/1/sprints/2/user_stories/3"));
    assertTrue(strategy.supports("/projects/10/sprints/20/user_stories/30"));
  }

  @Test
  void unsupportedUrlTest() {
    // Test invalid URLs
    assertFalse(strategy.supports("/projects/1"));
    assertFalse(strategy.supports("/invalid/path"));
    assertFalse(strategy.supports("/projects/abc/invalid"));
  }

  @Test
  void validateTest() {
    // Test validation is called with correct parameters
    String url = "/projects/1/sprints/2/user_stories/3";
    doNothing().when(sprintInProjectValidator).validate(anyLong(), anyLong());
    doNothing().when(userStoryInSprintValidator).validate(anyLong(), anyLong());

    strategy.validate(url);

    verify(sprintInProjectValidator).validate(1L, 2L);
    verify(userStoryInSprintValidator).validate(2L, 3L);
  }
}
