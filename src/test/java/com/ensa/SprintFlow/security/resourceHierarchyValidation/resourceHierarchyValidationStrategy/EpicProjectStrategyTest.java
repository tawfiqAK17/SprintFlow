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
class EpicProjectStrategyTest {

  private EpicProjectStrategy strategy;
  private AntPathMatcher pathMatcher;

  @Mock private EpicInProjectValidator epicInProjectValidator;

  @BeforeEach
  void setUp() {
    pathMatcher = new AntPathMatcher();
    strategy = new EpicProjectStrategy(pathMatcher, epicInProjectValidator);
  }

  @Test
  void supportedUrlTest() {
    // Test valid URLs
    assertTrue(strategy.supports("/projects/1/epics/2"));
    assertTrue(strategy.supports("/projects/100/epics/200"));
    assertTrue(strategy.supports("/projects/1/epics/1"));
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
    String url = "/projects/1/epics/2";
    doNothing().when(epicInProjectValidator).validate(anyLong(), anyLong());

    strategy.validate(url);

    verify(epicInProjectValidator).validate(1L, 2L);
  }
}
