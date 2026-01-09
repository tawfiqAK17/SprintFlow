package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.*;
import com.ensa.SprintFlow.service.ProjectMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.AntPathMatcher;

@ExtendWith(MockitoExtension.class)
class UserStoryProjectStrategyTest {

    private UserStoryProjectStrategy strategy;
    private AntPathMatcher pathMatcher;
    
    @Mock
    private UserStoryInProjectValidator userStoryInProjectValidator;

    @BeforeEach
    void setUp() {
        pathMatcher = new AntPathMatcher();
        strategy = new UserStoryProjectStrategy(pathMatcher, userStoryInProjectValidator);
    }

    @Test
    void supportedUrlTest() {
        // Test valid URLs without query params
        assertTrue(strategy.supports("/projects/1/user_stories/2"));
        assertTrue(strategy.supports("/projects/100/user_stories/200"));
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
        String url = "/projects/1/user_stories/2";
        doNothing().when(userStoryInProjectValidator).validate(anyLong(), anyLong());
        
        strategy.validate(url);
        
        verify(userStoryInProjectValidator).validate(1L, 2L);
    }
}
