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
class UserProjectStrategyTest {

    private UserProjectStrategy strategy;
    private AntPathMatcher pathMatcher;
    
    @Mock
    private ProjectMemberService projectMemberService;
    @Mock
    private UserInProjectValidator userInProjectValidator;

    @BeforeEach
    void setUp() {
        pathMatcher = new AntPathMatcher();
        strategy = new UserProjectStrategy(pathMatcher, projectMemberService, userInProjectValidator);
    }

    @Test
    void supportedUrlTest() {
        // Test valid URLs with wildcard
        assertTrue(strategy.supports("/projects/1/members/john"));
        assertTrue(strategy.supports("/projects/1/members/john/permissions"));
        assertTrue(strategy.supports("/projects/1/members/alice/roles"));
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
        String url = "/projects/1/members/john";
        doNothing().when(userInProjectValidator).validate(anyLong(), anyString());
        
        strategy.validate(url);
        
        verify(userInProjectValidator).validate(1L, "john");
    }
}
