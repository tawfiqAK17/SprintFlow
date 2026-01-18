package com.ensa.SprintFlow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.SprintFlow.dto.project.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.project.request.ProjectUpdateRequestDto;
import com.ensa.SprintFlow.dto.projectMember.request.ProjectMemberRequestDto;
import com.ensa.SprintFlow.dto.projectMember.response.ProjectMemberResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.ProjectMapper;
import com.ensa.SprintFlow.mapper.ProjectMemberMapper;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.repository.ProjectRepository;
import com.ensa.SprintFlow.security.model.UserContext;
import com.ensa.SprintFlow.security.service.UserAuthorizationService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

  @Mock private ProjectMapper mapper;

  @Mock private ProjectRepository projectRepository;

  @Mock private ProjectMemberService projectMemberService;

  @Mock private EpicService epicService;

  @Mock private ProjectMemberMapper projectMemberMapper;

  @Mock private UserAuthorizationService userAuthorizationService;

  @Mock private SecurityContext securityContext;

  @Mock private Authentication authentication;

  @InjectMocks private ProjectService projectService;

  private Project project;
  private ProjectRequestDto projectRequestDto;
  private ProjectUpdateRequestDto projectUpdateRequestDto;
  private ProjectMemberRequestDto projectMemberRequestDto;
  private ProjectMemberResponseDto projectMemberResponseDto;
  private Epic defaultEpic;
  private UserContext userContext;
  private User user;
  private ProjectMember projectMember;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setUsername("testuser");
    user.setEmail("test@example.com");

    userContext = new UserContext(user);

    projectRequestDto =
        ProjectRequestDto.builder()
            .name("Test Project")
            .description("Test Description")
            .scrumMasterUsername("scrummaster")
            .build();

    projectUpdateRequestDto =
        ProjectUpdateRequestDto.builder()
            .name("Updated Project")
            .description("Updated Description")
            .scrumMasterUsername("newscrummaster")
            .build();

    project =
        Project.builder()
            .id(1L)
            .name("Test Project")
            .description("Test Description")
            .creationDate(LocalDateTime.now())
            .projectMembers(new ArrayList<>())
            .epics(new ArrayList<>())
            .sprints(new ArrayList<>())
            .build();

    defaultEpic =
        Epic.builder()
            .id(1L)
            .title("Global Epic")
            .description("the default epic for the project")
            .project(project)
            .userStories(new ArrayList<>())
            .build();

    User memberUser = new User();
    memberUser.setId(2L);
    memberUser.setUsername("member1");
    memberUser.setEmail("member1@example.com");

    projectMember =
        ProjectMember.builder()
            .id(1L)
            .project(project)
            .user(memberUser)
            .userRole(Role.DEVELOPER)
            .build();

    projectMemberRequestDto =
        ProjectMemberRequestDto.builder().username("newmember").role(Role.DEVELOPER).build();

    projectMemberResponseDto =
        ProjectMemberResponseDto.builder()
            .username("member1")
            .email("member1@example.com")
            .role(Role.DEVELOPER)
            .build();
  }

  @Test
  void save_ShouldCreateProjectWithDefaultEpicAndMembers() {
    // Given
    try (MockedStatic<SecurityContextHolder> mockedSecurityContext =
        mockStatic(SecurityContextHolder.class)) {
      mockedSecurityContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      when(authentication.getPrincipal()).thenReturn(userContext);

      when(mapper.mapToProject(projectRequestDto)).thenReturn(project);
      when(projectRepository.save(any(Project.class))).thenReturn(project);
      when(epicService.save(any(Epic.class))).thenReturn(defaultEpic);

      // When
      Project result = projectService.save(projectRequestDto);

      // Then
      assertNotNull(result);
      assertEquals(project.getId(), result.getId());
      assertEquals(project.getName(), result.getName());
      assertNotNull(result.getCreationDate());
      assertEquals(defaultEpic, result.getDefaultEpic());

      verify(mapper).mapToProject(projectRequestDto);
      verify(projectRepository).save(any(Project.class));
      verify(projectMemberService).saveAny(project, "testuser", Role.PRODUCT_OWNER);
      verify(projectMemberService).saveAny(project, "scrummaster", Role.SCRUM_MASTER);
      verify(epicService).save(any(Epic.class));
    }
  }

  @Test
  void update_WithAllFields_ShouldUpdateProjectSuccessfully() {
    // Given
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    when(projectRepository.save(project)).thenReturn(project);

    // When
    Project result = projectService.update(1L, projectUpdateRequestDto);

    // Then
    assertNotNull(result);
    verify(projectRepository).findById(1L);
    verify(projectRepository).save(project);
    verify(projectMemberService).deleteProjectScrumMaster(1L);
    verify(projectMemberService).saveAny(project, "newscrummaster", Role.SCRUM_MASTER);
  }

  @Test
  void update_WithPartialFields_ShouldUpdateOnlyProvidedFields() {
    // Given
    ProjectUpdateRequestDto partialUpdate =
        ProjectUpdateRequestDto.builder().name("Partial Update").build();

    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    when(projectRepository.save(project)).thenReturn(project);

    // When
    Project result = projectService.update(1L, partialUpdate);

    // Then
    assertNotNull(result);
    verify(projectRepository).findById(1L);
    verify(projectRepository).save(project);
    verify(projectMemberService, never()).deleteProjectScrumMaster(anyLong());
  }

  @Test
  void update_WhenProjectNotFound_ShouldThrowNotFoundException() {
    // Given
    when(projectRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        NotFoundException.class, () -> projectService.update(999L, projectUpdateRequestDto));

    verify(projectRepository).findById(999L);
    verify(projectRepository, never()).save(any());
  }

  @Test
  void getProjects_ShouldReturnAllProjects() {
    // Given
    Project project2 =
        Project.builder()
            .id(2L)
            .name("Project 2")
            .description("Description 2")
            .creationDate(LocalDateTime.now())
            .build();

    List<Project> projects = Arrays.asList(project, project2);
    when(projectRepository.findAll()).thenReturn(projects);

    // When
    List<Project> result = projectService.getProjects();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(project.getId(), result.get(0).getId());
    assertEquals(project2.getId(), result.get(1).getId());
    verify(projectRepository).findAll();
  }

  @Test
  void getProject_WhenProjectExists_ShouldReturnProject() {
    // Given
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

    // When
    Project result = projectService.getProject(1L);

    // Then
    assertNotNull(result);
    assertEquals(project.getId(), result.getId());
    verify(projectRepository).findById(1L);
  }

  @Test
  void getProject_WhenProjectNotFound_ShouldThrowNotFoundException() {
    // Given
    when(projectRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> projectService.getProject(999L));

    assertEquals("no project found with the given id", exception.getMessage());
    verify(projectRepository).findById(999L);
  }

  @Test
  void findById_WhenProjectExists_ShouldReturnProject() {
    // Given
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

    // When
    Project result = projectService.findById(1L);

    // Then
    assertNotNull(result);
    assertEquals(project.getId(), result.getId());
    verify(projectRepository).findById(1L);
  }

  @Test
  void findById_WhenProjectNotFound_ShouldThrowNotFoundException() {
    // Given
    when(projectRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> projectService.findById(999L));

    assertEquals("no project found with the given id", exception.getMessage());
    verify(projectRepository).findById(999L);
  }

  @Test
  void deleteProject_ShouldDeleteProjectById() {
    // When
    projectService.deleteProject(1L);

    // Then
    verify(projectRepository).deleteById(1L);
  }

  @Test
  void saveMember_ShouldSaveProjectMember() {
    // Given
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

    // When
    projectService.saveMember(1L, projectMemberRequestDto);

    // Then
    verify(projectRepository).findById(1L);
    verify(projectMemberService).save(project, projectMemberRequestDto);
  }

  @Test
  void saveMember_WhenProjectNotFound_ShouldThrowNotFoundException() {
    // Given
    when(projectRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        NotFoundException.class, () -> projectService.saveMember(999L, projectMemberRequestDto));

    verify(projectRepository).findById(999L);
    verify(projectMemberService, never()).save(any(), any());
  }

  @Test
  void addRoleToMember_ShouldAddRoleToExistingMember() {
    // Given
    List<ProjectMember> members = Arrays.asList(projectMember);
    when(projectMemberService.findAllByProjectIdAndUsername(1L, "member1")).thenReturn(members);
    when(userAuthorizationService.getAuthenticatedUser()).thenReturn(userContext);
    userContext.setProject(project);

    // When
    projectService.addRoleToMember(1L, "member1", Role.SCRUM_MASTER);

    // Then
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "member1");
    verify(projectMemberService).save(eq(project), any(ProjectMemberRequestDto.class));
  }

  @Test
  void addRoleToMember_WhenMemberNotFound_ShouldThrowNotFoundException() {
    // Given
    when(projectMemberService.findAllByProjectIdAndUsername(1L, "nonexistent"))
        .thenReturn(new ArrayList<>());

    // When & Then
    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> projectService.addRoleToMember(1L, "nonexistent", Role.DEVELOPER));

    assertEquals(
        "the project does not have any member with the given username", exception.getMessage());
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "nonexistent");
  }

  @Test
  void removeRoleFromMember_ShouldRemoveRoleFromMember() {
    // Given
    List<ProjectMember> members = Arrays.asList(projectMember);
    when(projectMemberService.findAllByProjectIdAndUsername(1L, "member1")).thenReturn(members);

    // When
    projectService.removeRoleFromMember(1L, "member1", Role.DEVELOPER);

    // Then
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "member1");
    verify(projectMemberService).deleteRelation(1L);
  }

  @Test
  void removeRoleFromMember_WhenMemberOrRoleNotFound_ShouldThrowNotFoundException() {
    // Given
    List<ProjectMember> members = Arrays.asList(projectMember);
    when(projectMemberService.findAllByProjectIdAndUsername(1L, "member1")).thenReturn(members);

    // When & Then
    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> projectService.removeRoleFromMember(1L, "member1", Role.SCRUM_MASTER));

    assertEquals(
        "the project does not have any member with the given username and role",
        exception.getMessage());
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "member1");
    verify(projectMemberService, never()).deleteRelation(anyLong());
  }

  @Test
  void removeMember_ShouldRemoveAllMemberRelations() {
    // Given
    ProjectMember projectMember2 =
        ProjectMember.builder()
            .id(2L)
            .project(project)
            .user(projectMember.getUser())
            .userRole(Role.SCRUM_MASTER)
            .build();

    List<ProjectMember> members = Arrays.asList(projectMember, projectMember2);
    when(projectMemberService.findAllByProjectIdAndUsername(1L, "member1")).thenReturn(members);

    // When
    projectService.removeMember(1L, "member1");

    // Then
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "member1");
    verify(projectMemberService, times(2)).deleteRelation(anyLong());
    verify(projectMemberService).deleteRelation(1L);
    verify(projectMemberService).deleteRelation(2L);
  }

  @Test
  void getProjectMembers_WithNoRoleFilter_ShouldReturnAllMembers() {
    // Given
    List<ProjectMember> members = Arrays.asList(projectMember);
    when(projectMemberService.findAllByProjectId(1L)).thenReturn(members);
    when(projectMemberMapper.mapToProjectMemberResponseDto(projectMember))
        .thenReturn(projectMemberResponseDto);

    // When
    List<ProjectMemberResponseDto> result = projectService.getProjectMembers(1L, null);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(projectMemberResponseDto.getUsername(), result.get(0).getUsername());
    verify(projectMemberService).findAllByProjectId(1L);
    verify(projectMemberMapper).mapToProjectMemberResponseDto(projectMember);
  }

  @Test
  void getProjectMembers_WithRoleFilter_ShouldReturnFilteredMembers() {
    // Given
    List<ProjectMember> developers = Arrays.asList(projectMember);
    when(projectMemberService.findAllByProjectIdAndUserRole(1L, Role.DEVELOPER))
        .thenReturn(developers);
    when(projectMemberMapper.mapToProjectMemberResponseDto(projectMember))
        .thenReturn(projectMemberResponseDto);

    // When
    List<ProjectMemberResponseDto> result = projectService.getProjectMembers(1L, Role.DEVELOPER);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(Role.DEVELOPER, result.get(0).getRole());
    verify(projectMemberService).findAllByProjectIdAndUserRole(1L, Role.DEVELOPER);
    verify(projectMemberMapper).mapToProjectMemberResponseDto(projectMember);
  }

  @Test
  void getProjectMembers_WhenNoMembersExist_ShouldReturnEmptyList() {
    // Given
    when(projectMemberService.findAllByProjectId(1L)).thenReturn(new ArrayList<>());

    // When
    List<ProjectMemberResponseDto> result = projectService.getProjectMembers(1L, null);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(projectMemberService).findAllByProjectId(1L);
  }
}
