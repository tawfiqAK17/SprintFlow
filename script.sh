#!/bin/bash

# Script to generate test classes for ResourceHierarchyValidationStrategy implementations
# Usage: ./generate_tests.sh

# Base package path
BASE_PACKAGE="com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy"
TEST_DIR="src/test/java/com/ensa/SprintFlow/security/resourceHierarchyValidation/resourceHierarchyValidationStrategy"

# Create test directory if it doesn't exist
mkdir -p "$TEST_DIR"

# Function to generate test class
generate_test() {
    local class_name=$1
    local url_pattern=$2
    local query_params=$3
    local test_file="${TEST_DIR}/${class_name}Test.java"
    
    # Get the content parts
    local mock_fields=$(get_mock_fields "$class_name")
    local strategy_init=$(get_strategy_initialization "$class_name")
    local supported_tests=$(get_supported_url_tests "$url_pattern" "$query_params")
    local unsupported_tests=$(get_unsupported_url_tests "$url_pattern" "$query_params")
    local validate_test=$(get_validate_test "$url_pattern" "$query_params" "$class_name")
    
    cat > "$test_file" << EOF
package ${BASE_PACKAGE};

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import ${BASE_PACKAGE}.validators.*;
import com.ensa.SprintFlow.service.ProjectMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.AntPathMatcher;

@ExtendWith(MockitoExtension.class)
class ${class_name}Test {

    private ${class_name} strategy;
    private AntPathMatcher pathMatcher;
    
${mock_fields}

    @BeforeEach
    void setUp() {
        pathMatcher = new AntPathMatcher();
        ${strategy_init}
    }

    @Test
    void supportedUrlTest() {
${supported_tests}
    }

    @Test
    void unsupportedUrlTest() {
${unsupported_tests}
    }
    
    @Test
    void validateTest() {
${validate_test}
    }
}
EOF
    
    echo "Generated: $test_file"
}

# Helper function to get mock fields
get_mock_fields() {
    case $1 in
        "EpicProjectStrategy")
            echo "    @Mock
    private EpicInProjectValidator epicInProjectValidator;"
            ;;
        "SprintProjectStrategy")
            echo "    @Mock
    private SprintInProjectValidator sprintInProjectValidator;"
            ;;
        "UserProjectStrategy")
            echo "    @Mock
    private ProjectMemberService projectMemberService;
    @Mock
    private UserInProjectValidator userInProjectValidator;"
            ;;
        "UserStoryEpicProjectStrategy")
            echo "    @Mock
    private EpicInProjectValidator epicInProjectValidator;
    @Mock
    private UserStoryInEpicValidator userStoryInEpicValidator;"
            ;;
        "UserStoryProjectStrategy")
            echo "    @Mock
    private UserStoryInProjectValidator userStoryInProjectValidator;"
            ;;
        "UserStorySprintEpicProjectStrategy")
            echo "    @Mock
    private EpicInProjectValidator epicInProjectValidator;
    @Mock
    private SprintInProjectValidator sprintInProjectValidator;
    @Mock
    private UserStoryInEpicValidator userStoryInEpicValidator;
    @Mock
    private UserStoryInSprintValidator userStoryInSprintValidator;"
            ;;
        "UserStorySprintProjectStrategy")
            echo "    @Mock
    private UserStoryInSprintValidator userStoryInSprintValidator;
    @Mock
    private SprintInProjectValidator sprintInProjectValidator;"
            ;;
    esac
}

# Helper function to get strategy initialization
get_strategy_initialization() {
    case $1 in
        "EpicProjectStrategy")
            echo 'strategy = new EpicProjectStrategy(pathMatcher, epicInProjectValidator);'
            ;;
        "SprintProjectStrategy")
            echo 'strategy = new SprintProjectStrategy(pathMatcher, sprintInProjectValidator);'
            ;;
        "UserProjectStrategy")
            echo 'strategy = new UserProjectStrategy(pathMatcher, projectMemberService, userInProjectValidator);'
            ;;
        "UserStoryEpicProjectStrategy")
            echo 'strategy = new UserStoryEpicProjectStrategy(pathMatcher, epicInProjectValidator, userStoryInEpicValidator);'
            ;;
        "UserStoryProjectStrategy")
            echo 'strategy = new UserStoryProjectStrategy(pathMatcher, userStoryInProjectValidator);'
            ;;
        "UserStorySprintEpicProjectStrategy")
            echo 'strategy = new UserStorySprintEpicProjectStrategy(pathMatcher, epicInProjectValidator, sprintInProjectValidator, userStoryInEpicValidator, userStoryInSprintValidator);'
            ;;
        "UserStorySprintProjectStrategy")
            echo 'strategy = new UserStorySprintProjectStrategy(pathMatcher, userStoryInSprintValidator, sprintInProjectValidator);'
            ;;
    esac
}

# Helper function to get supported URL tests
get_supported_url_tests() {
    local pattern=$1
    local query_params=$2
    
    case $pattern in
        "/projects/{projectId}/epics/{epicId}")
            echo '        // Test valid URLs
        assertTrue(strategy.supports("/projects/1/epics/2"));
        assertTrue(strategy.supports("/projects/100/epics/200"));
        assertTrue(strategy.supports("/projects/1/epics/1"));'
            ;;
        "/projects/{projectId}/sprints/{sprintId}")
            echo '        // Test valid URLs
        assertTrue(strategy.supports("/projects/1/sprints/2"));
        assertTrue(strategy.supports("/projects/100/sprints/200"));
        assertTrue(strategy.supports("/projects/5/sprints/10"));'
            ;;
        "/projects/{projectId}/members/{username}/**")
            echo '        // Test valid URLs with wildcard
        assertTrue(strategy.supports("/projects/1/members/john"));
        assertTrue(strategy.supports("/projects/1/members/john/permissions"));
        assertTrue(strategy.supports("/projects/1/members/alice/roles"));'
            ;;
        "/projects/{projectId}/user_stories/{userStoryId}")
            if [[ $query_params == *"epic_id"* ]] && [[ $query_params == *"sprint_id"* ]]; then
                echo '        // Test valid URLs with both query params
        assertTrue(strategy.supports("/projects/1/user_stories/2?epic_id=3&sprint_id=4"));
        assertTrue(strategy.supports("/projects/1/user_stories/2?sprint_id=4&epic_id=3"));'
            elif [[ $query_params == *"epic_id"* ]]; then
                echo '        // Test valid URLs with epic_id query param
        assertTrue(strategy.supports("/projects/1/user_stories/2?epic_id=3"));
        assertTrue(strategy.supports("/projects/100/user_stories/200?epic_id=300"));'
            else
                echo '        // Test valid URLs without query params
        assertTrue(strategy.supports("/projects/1/user_stories/2"));
        assertTrue(strategy.supports("/projects/100/user_stories/200"));'
            fi
            ;;
        "/projects/{projectId}/sprints/{sprintId}/user_stories/{userStoryId}")
            echo '        // Test valid URLs
        assertTrue(strategy.supports("/projects/1/sprints/2/user_stories/3"));
        assertTrue(strategy.supports("/projects/10/sprints/20/user_stories/30"));'
            ;;
    esac
}

# Helper function to get unsupported URL tests
get_unsupported_url_tests() {
    local pattern=$1
    local query_params=$2
    
    local base_tests='        // Test invalid URLs
        assertFalse(strategy.supports("/projects/1"));
        assertFalse(strategy.supports("/invalid/path"));
        assertFalse(strategy.supports("/projects/abc/invalid"));'
    
    if [[ $query_params == *"epic_id"* ]] && [[ $query_params == *"sprint_id"* ]]; then
        echo "$base_tests"
        echo '        
        // Test missing query params
        assertFalse(strategy.supports("/projects/1/user_stories/2"));
        assertFalse(strategy.supports("/projects/1/user_stories/2?epic_id=3"));
        assertFalse(strategy.supports("/projects/1/user_stories/2?sprint_id=4"));
        
        // Test extra query params
        assertFalse(strategy.supports("/projects/1/user_stories/2?epic_id=3&sprint_id=4&extra=5"));'
    elif [[ $query_params == *"epic_id"* ]]; then
        echo "$base_tests"
        echo '        
        // Test missing query params
        assertFalse(strategy.supports("/projects/1/user_stories/2"));
        
        // Test wrong query params
        assertFalse(strategy.supports("/projects/1/user_stories/2?sprint_id=3"));
        assertFalse(strategy.supports("/projects/1/user_stories/2?epic_id=3&sprint_id=4"));'
    else
        echo "$base_tests"
    fi
}

# Helper function to get validate test
get_validate_test() {
    local pattern=$1
    local query_params=$2
    local class_name=$3
    
    case $class_name in
        "EpicProjectStrategy")
            echo '        // Test validation is called with correct parameters
        String url = "/projects/1/epics/2";
        doNothing().when(epicInProjectValidator).validate(anyLong(), anyLong());
        
        strategy.validate(url);
        
        verify(epicInProjectValidator).validate(1L, 2L);'
            ;;
        "SprintProjectStrategy")
            echo '        // Test validation is called with correct parameters
        String url = "/projects/1/sprints/2";
        doNothing().when(sprintInProjectValidator).validate(anyLong(), anyLong());
        
        strategy.validate(url);
        
        verify(sprintInProjectValidator).validate(1L, 2L);'
            ;;
        "UserProjectStrategy")
            echo '        // Test validation is called with correct parameters
        String url = "/projects/1/members/john";
        doNothing().when(userInProjectValidator).validate(anyLong(), anyString());
        
        strategy.validate(url);
        
        verify(userInProjectValidator).validate(1L, "john");'
            ;;
        "UserStoryEpicProjectStrategy")
            echo '        // Test validation is called with correct parameters
        String url = "/projects/1/user_stories/2?epic_id=3";
        doNothing().when(epicInProjectValidator).validate(anyLong(), anyLong());
        doNothing().when(userStoryInEpicValidator).validate(anyLong(), anyLong());
        
        strategy.validate(url);
        
        verify(epicInProjectValidator).validate(1L, 3L);
        verify(userStoryInEpicValidator).validate(3L, 2L);'
            ;;
        "UserStoryProjectStrategy")
            echo '        // Test validation is called with correct parameters
        String url = "/projects/1/user_stories/2";
        doNothing().when(userStoryInProjectValidator).validate(anyLong(), anyLong());
        
        strategy.validate(url);
        
        verify(userStoryInProjectValidator).validate(1L, 2L);'
            ;;
        "UserStorySprintEpicProjectStrategy")
            echo '        // Test validation is called with correct parameters
        String url = "/projects/1/user_stories/2?epic_id=3&sprint_id=4";
        doNothing().when(epicInProjectValidator).validate(anyLong(), anyLong());
        doNothing().when(sprintInProjectValidator).validate(anyLong(), anyLong());
        doNothing().when(userStoryInEpicValidator).validate(anyLong(), anyLong());
        doNothing().when(userStoryInSprintValidator).validate(anyLong(), anyLong());
        
        strategy.validate(url);
        
        verify(epicInProjectValidator).validate(1L, 3L);
        verify(sprintInProjectValidator).validate(1L, 4L);
        verify(userStoryInEpicValidator).validate(3L, 2L);
        verify(userStoryInSprintValidator).validate(4L, 2L);'
            ;;
        "UserStorySprintProjectStrategy")
            echo '        // Test validation is called with correct parameters
        String url = "/projects/1/sprints/2/user_stories/3";
        doNothing().when(sprintInProjectValidator).validate(anyLong(), anyLong());
        doNothing().when(userStoryInSprintValidator).validate(anyLong(), anyLong());
        
        strategy.validate(url);
        
        verify(sprintInProjectValidator).validate(1L, 2L);
        verify(userStoryInSprintValidator).validate(2L, 3L);'
            ;;
    esac
}

# Generate tests for each strategy
echo "Generating test classes..."

generate_test "EpicProjectStrategy" "/projects/{projectId}/epics/{epicId}" ""
generate_test "SprintProjectStrategy" "/projects/{projectId}/sprints/{sprintId}" ""
generate_test "UserProjectStrategy" "/projects/{projectId}/members/{username}/**" ""
generate_test "UserStoryEpicProjectStrategy" "/projects/{projectId}/user_stories/{userStoryId}" "epic_id"
generate_test "UserStoryProjectStrategy" "/projects/{projectId}/user_stories/{userStoryId}" ""
generate_test "UserStorySprintEpicProjectStrategy" "/projects/{projectId}/user_stories/{userStoryId}" "epic_id,sprint_id"
generate_test "UserStorySprintProjectStrategy" "/projects/{projectId}/sprints/{sprintId}/user_stories/{userStoryId}" ""

echo ""
echo "Test generation complete!"
echo "Generated test files in: $TEST_DIR"
