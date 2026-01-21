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
