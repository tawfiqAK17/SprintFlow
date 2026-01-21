#!/bin/bash

# MapStruct Mapper Recreation Script
# This script recreates mapper classes using MapStruct @Mapper annotation

BASE_DIR="src/main/java/com/ensa/SprintFlow/mapper"

# Create directory if it doesn't exist
mkdir -p "$BASE_DIR"

# 1. AcceptanceCriteriaMapper
cat > "$BASE_DIR/AcceptanceCriteriaMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.acceptanceCriteria.AcceptanceCriteriaDto;
import com.ensa.SprintFlow.model.AcceptanceCriteria;
import com.ensa.SprintFlow.model.And;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AcceptanceCriteriaMapper {

    @Mapping(target = "givenWhat", source = "given")
    @Mapping(target = "whenWhat", source = "when")
    @Mapping(target = "ands", source = "ands", qualifiedByName = "stringsToAnds")
    @Mapping(target = "thenWhat", source = "then")
    AcceptanceCriteria mapToAcceptanceCriteria(AcceptanceCriteriaDto dto);

    @Mapping(target = "given", source = "givenWhat")
    @Mapping(target = "when", source = "whenWhat")
    @Mapping(target = "ands", source = "ands", qualifiedByName = "andsToStrings")
    @Mapping(target = "then", source = "thenWhat")
    AcceptanceCriteriaDto mapToAcceptanceCriteriaDto(AcceptanceCriteria acceptanceCriteria);

    List<AcceptanceCriteriaDto> mapToAcceptanceCriteriaDto(List<AcceptanceCriteria> criteriaList);

    @Named("stringsToAnds")
    default Set<And> stringsToAnds(Set<String> strings) {
        if (strings == null) return null;
        return strings.stream()
            .map(s -> And.builder().andWhat(s).build())
            .collect(Collectors.toSet());
    }

    @Named("andsToStrings")
    default Set<String> andsToStrings(Set<And> ands) {
        if (ands == null) return null;
        return ands.stream()
            .map(And::getAndWhat)
            .collect(Collectors.toSet());
    }
}
EOF

# 2. EpicMapper
cat > "$BASE_DIR/EpicMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.epic.request.EpicRequestDto;
import com.ensa.SprintFlow.dto.epic.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.dto.epic.response.EpicResponseDto;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EpicMapper {

    @Mapping(target = "project", source = "project")
    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "description", source = "dto.description")
    Epic mapToEpic(Project project, EpicRequestDto dto);

    EpicMetaDataResponseDto mapToEpicMetaDataResponseDto(Epic epic);

    @Mapping(target = "userStoriesCount", expression = "java(epic.getUserStories() != null ? epic.getUserStories().size() : 0)")
    EpicResponseDto mapToEpicResponseDto(Epic epic);
}
EOF

# 3. ProjectMapper
cat > "$BASE_DIR/ProjectMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.project.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.project.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.dto.project.response.ProjectResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.ProjectMemberService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ProjectMapper {

    @Autowired
    protected ProjectMemberService projectMemberService;

    public abstract Project mapToProject(ProjectRequestDto dto);

    @Mapping(target = "scrumMaster", ignore = true)
    @Mapping(target = "productOwner", ignore = true)
    @Mapping(target = "userRole", constant = "PRODUCT_OWNER")
    public abstract ProjectMetaDataResponseDto mapToMetaDataResponseDto(Project project);

    @AfterMapping
    protected void enrichMetaData(@MappingTarget ProjectMetaDataResponseDto dto, Project project) {
        User scrumMaster = projectMemberService.getProjectScrumMaster(project.getId());
        User productOwner = projectMemberService.getProjectProductOwner(project.getId());
        if (scrumMaster != null) {
            dto.setScrumMaster(mapUserToMetaData(scrumMaster));
        }
        if (productOwner != null) {
            dto.setProductOwner(mapUserToMetaData(productOwner));
        }
    }

    @Mapping(target = "scrumMaster", ignore = true)
    @Mapping(target = "productOwner", ignore = true)
    @Mapping(target = "members", ignore = true)
    public abstract ProjectResponseDto mapToResponseDto(Project project);

    @AfterMapping
    protected void enrichResponseDto(@MappingTarget ProjectResponseDto dto, Project project) {
        dto.setScrumMaster(mapUserToMetaData(projectMemberService.getProjectScrumMaster(project.getId())));
        dto.setProductOwner(mapUserToMetaData(projectMemberService.getProjectProductOwner(project.getId())));

        List<ProjectMember> projectMembers = projectMemberService.getAllMembers(project.getId()).stream()
            .filter(m -> !m.getUserRole().equals(Role.SCRUM_MASTER) && !m.getUserRole().equals(Role.PRODUCT_OWNER))
            .toList();

        List<ProjectResponseDto.Member> members = projectMembers.stream()
            .map(m -> new ProjectResponseDto.Member(mapUserToMetaData(m.getUser()), m.getUserRole()))
            .toList();
        dto.setMembers(members);
    }

    protected abstract com.ensa.SprintFlow.dto.user.response.UserMetaDataResponseDto mapUserToMetaData(User user);
}
EOF

# 4. ProjectMemberMapper
cat > "$BASE_DIR/ProjectMemberMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.projectMember.response.ProjectMemberResponseDto;
import com.ensa.SprintFlow.model.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "role", source = "userRole")
    ProjectMemberResponseDto mapToProjectMemberResponseDto(ProjectMember projectMember);
}
EOF

# 5. RegisterMapper
cat > "$BASE_DIR/RegisterMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.security.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    @Mapping(target = "verified", constant = "false")
    User mapToUser(RegisterRequestDto dto);
}
EOF

# 6. ReportMapper
cat > "$BASE_DIR/ReportMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.report.response.ReportResponseDto;
import com.ensa.SprintFlow.model.Report;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    ReportResponseDto mapToReportResponseDto(Report report);

    List<ReportResponseDto> mapToReportResponseDto(List<Report> reports);
}
EOF

# 7. SprintMapper
cat > "$BASE_DIR/SprintMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.sprint.request.SprintRequestDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintMetaDataResponseDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintResponseDto;
import com.ensa.SprintFlow.model.Sprint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SprintMapper {

    SprintMetaDataResponseDto mapToSprintMetaDadaResponseDto(Sprint sprint);

    Sprint mapToSprint(SprintRequestDto dto);

    @Mapping(target = "userStoriesCount", expression = "java(sprint.getUserStories() != null ? sprint.getUserStories().size() : 0)")
    SprintResponseDto maptoSprintResponseDto(Sprint sprint);
}
EOF

# 8. TaskMapper
cat > "$BASE_DIR/TaskMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.task.request.TaskRequestDto;
import com.ensa.SprintFlow.dto.task.response.TaskDetailsResponseDto;
import com.ensa.SprintFlow.dto.task.response.TaskMetaDataResponseDto;
import com.ensa.SprintFlow.model.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserStoryMapper.class, UserMapper.class, ReportMapper.class})
public interface TaskMapper {

    Task mapToTaskRequestDto(TaskRequestDto taskRequestDto);

    TaskMetaDataResponseDto mapToTaskMetaDataResponseDto(Task task);

    List<TaskMetaDataResponseDto> mapToTaskMetaDataResponseDto(List<Task> taskList);

    TaskDetailsResponseDto mapToTaskDetailsResponseDto(Task task);
}
EOF

# 9. UserMapper
cat > "$BASE_DIR/UserMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.user.response.UserMetaDataResponseDto;
import com.ensa.SprintFlow.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMetaDataResponseDto mapToMetaDataDto(User user);
}
EOF

# 10. UserStoryDescriptionMapper
cat > "$BASE_DIR/UserStoryDescriptionMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.userStoryDescription.UserStoryDescriptionDto;
import com.ensa.SprintFlow.model.UserStoryDescription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStoryDescriptionMapper {

    UserStoryDescriptionDto mapToUserStoryDescriptionDto(UserStoryDescription userStoryDescription);

    UserStoryDescription mapToUserStoryDescription(UserStoryDescriptionDto dto);
}
EOF

# 11. UserStoryMapper
cat > "$BASE_DIR/UserStoryMapper.java" << 'EOF'
package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
import com.ensa.SprintFlow.model.UserStory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EpicMapper.class, SprintMapper.class, UserStoryDescriptionMapper.class, AcceptanceCriteriaMapper.class})
public interface UserStoryMapper {

    @Mapping(target = "description", source = "userStoryDescription")
    UserStoryResponseDto mapToUserStoryResponseDto(UserStory userStory);

    UserStoryMetaDataResponseDto mapToUserStoryMetaDataResponseDto(UserStory userStory);

    @Mapping(target = "userStoryDescription", source = "description")
    UserStory mapToUserStory(UserStoryRequestDto dto);
}
EOF

echo "✓ All mapper files created successfully!"
echo ""
echo "Next steps:"
echo "1. Add MapStruct dependency to your pom.xml:"
echo ""
cat << 'POMEOF'
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
</dependency>
POMEOF
echo ""
echo "2. Add MapStruct processor to your maven-compiler-plugin:"
echo ""
cat << 'PLUGINEOF'
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.5.Final</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.30</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok-mapstruct-binding</artifactId>
                <version>0.2.0</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
PLUGINEOF
echo ""
echo "3. Run: mvn clean compile"
echo "4. Delete old mapper implementations if they exist"
