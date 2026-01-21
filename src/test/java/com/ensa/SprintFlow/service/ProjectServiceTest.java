package com.ensa.SprintFlow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ensa.SprintFlow.dto.project.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.project.request.ProjectUpdateRequestDto;
import com.ensa.SprintFlow.dto.project.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.dto.project.response.ProjectResponseDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
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

  @Mock private ProjectMapper projectMapper;

  @Mock private ProjectRepository projectRepository;

  @Mock private ProjectMemberService projectMemberService;

  @Mock private EpicService epicService;

  @Mock private ProjectMemberMapper projectMemberMapper;

  @Mock private UserAuthorizationService userAuthorizationService;

  @InjectMocks private ProjectService projectService;

  private Project testProject;
  private ProjectRequestDto projectRequestDto;
  private ProjectUpdateRequestDto projectUpdateRequestDto;
  private ProjectMetaDataResponseDto projectMetaDataResponseDto;
  private ProjectResponseDto projectResponseDto;
  private Epic defaultEpic;
  private User testUser;
  private User scrumMasterUser;
  private UserContext userContext;
  private MockedStatic<SecurityContextHolder> securityContextHolderMock;

  @BeforeEach
  void setUp() {
    // Setup test user (product owner)
    testUser =
        User.builder()
            .id(1L)
            .username("testuser")
            .firstName("Test")
            .lastName("User")
            .email("test@example.com")
            .password("password")
            .verified(true)
            .enrollDate(LocalDateTime.now())
            .build();

    // Setup scrum master user
    scrumMasterUser =
        User.builder()
            .id(2L)
            .username("scrummaster")
            .firstName("Scrum")
            .lastName("Master")
            .email("scrum@example.com")
            .password("password")
            .verified(true)
            .enrollDate(LocalDateTime.now())
            .build();

    // Setup user context
    userContext = new UserContext(testUser);

    // Setup test project
    testProject =
        Project.builder()
            .id(1L)
            .name("Test Project")
            .description("Test Description")
            .creationDate(LocalDateTime.now())
            .build();

    // Setup default epic
    defaultEpic =
        Epic.builder()
            .id(1L)
            .title("Global Epic")
            .description("the default epic for the project")
            .project(testProject)
            .build();

    // Setup DTOs
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

    projectMetaDataResponseDto =
        ProjectMetaDataResponseDto.builder()
            .id(1L)
            .name("Test Project")
            .description("Test Description")
            .build();

    projectResponseDto =
        ProjectResponseDto.builder()
            .id(1L)
            .name("Test Project")
            .description("Test Description")
            .creationDate(LocalDateTime.now())
            .build();
  }

  @AfterEach
  void tearDown() {
    if (securityContextHolderMock != null) {
      securityContextHolderMock.close();
    }
  }

  @Test
  void save_ShouldCreateProjectWithAllMembers_WhenValidDataProvided() {
    // Arrange
    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);

    securityContextHolderMock = mockStatic(SecurityContextHolder.class);
    securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userContext);

    when(projectMapper.mapToProject(projectRequestDto)).thenReturn(testProject);
    when(projectRepository.save(any(Project.class))).thenReturn(testProject);
    when(epicService.save(any(Epic.class))).thenReturn(defaultEpic);
    when(projectMapper.mapToMetaDataResponseDto(testProject))
        .thenReturn(projectMetaDataResponseDto);

    ProjectMember productOwnerMember =
        ProjectMember.builder()
            .id(1L)
            .user(testUser)
            .project(testProject)
            .userRole(Role.PRODUCT_OWNER)
            .build();

    ProjectMember scrumMasterMember =
        ProjectMember.builder()
            .id(2L)
            .user(scrumMasterUser)
            .project(testProject)
            .userRole(Role.SCRUM_MASTER)
            .build();

    when(projectMemberService.saveAny(testProject, "testuser", Role.PRODUCT_OWNER))
        .thenReturn(productOwnerMember);
    when(projectMemberService.saveAny(testProject, "scrummaster", Role.SCRUM_MASTER))
        .thenReturn(scrumMasterMember);

    // Act
    ProjectMetaDataResponseDto result = projectService.save(projectRequestDto);

    // Assert
    assertNotNull(result);
    assertEquals(projectMetaDataResponseDto.getId(), result.getId());

    verify(projectMapper).mapToProject(projectRequestDto);
    verify(projectRepository, times(1)).save(any(Project.class));
    verify(projectMemberService).saveAny(testProject, "testuser", Role.PRODUCT_OWNER);
    verify(projectMemberService).saveAny(testProject, "scrummaster", Role.SCRUM_MASTER);
    verify(epicService).save(any(Epic.class));
    verify(projectMapper).mapToMetaDataResponseDto(testProject);
  }

  @Test
  void save_ShouldSetCreationDate_WhenProjectIsSaved() {
    // Arrange
    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);

    securityContextHolderMock = mockStatic(SecurityContextHolder.class);
    securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userContext);

    Project projectWithoutDate =
        Project.builder().name("Test Project").description("Test Description").build();

    when(projectMapper.mapToProject(projectRequestDto)).thenReturn(projectWithoutDate);
    when(projectRepository.save(any(Project.class))).thenReturn(testProject);
    when(epicService.save(any(Epic.class))).thenReturn(defaultEpic);
    when(projectMapper.mapToMetaDataResponseDto(any(Project.class)))
        .thenReturn(projectMetaDataResponseDto);

    // Act
    projectService.save(projectRequestDto);

    // Assert
    assertNotNull(projectWithoutDate.getCreationDate());
  }

  @Test
  void save_ShouldSetDefaultEpic_WhenProjectIsSaved() {
    // Arrange
    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);

    securityContextHolderMock = mockStatic(SecurityContextHolder.class);
    securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userContext);

    when(projectMapper.mapToProject(projectRequestDto)).thenReturn(testProject);
    when(projectRepository.save(any(Project.class))).thenReturn(testProject);
    when(epicService.save(any(Epic.class))).thenReturn(defaultEpic);
    when(projectMapper.mapToMetaDataResponseDto(testProject))
        .thenReturn(projectMetaDataResponseDto);

    // Act
    projectService.save(projectRequestDto);

    // Assert
    verify(epicService)
        .save(
            argThat(
                epic ->
                    epic.getTitle().equals("Global Epic")
                        && epic.getDescription().equals("the default epic for the project")
                        && epic.getProject().equals(testProject)));
    assertEquals(defaultEpic, testProject.getDefaultEpic());
  }

  @Test
  void update_ShouldUpdateOnlyName_WhenOnlyNameProvided() {
    // Arrange
    ProjectUpdateRequestDto updateDto = ProjectUpdateRequestDto.builder().name("New Name").build();

    when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
    when(projectRepository.save(testProject)).thenReturn(testProject);
    when(projectMapper.mapToMetaDataResponseDto(testProject))
        .thenReturn(projectMetaDataResponseDto);

    // Act
    ProjectMetaDataResponseDto result = projectService.update(1L, updateDto);

    // Assert
    assertNotNull(result);
    assertEquals("New Name", testProject.getName());
    verify(projectRepository).save(testProject);
    verify(projectMemberService, never()).deleteProjectScrumMaster(anyLong());
  }

  @Test
  void update_ShouldUpdateOnlyDescription_WhenOnlyDescriptionProvided() {
    // Arrange
    ProjectUpdateRequestDto updateDto =
        ProjectUpdateRequestDto.builder().description("New Description").build();

    when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
    when(projectRepository.save(testProject)).thenReturn(testProject);
    when(projectMapper.mapToMetaDataResponseDto(testProject))
        .thenReturn(projectMetaDataResponseDto);

    // Act
    ProjectMetaDataResponseDto result = projectService.update(1L, updateDto);

    // Assert
    assertNotNull(result);
    assertEquals("New Description", testProject.getDescription());
    verify(projectRepository).save(testProject);
    verify(projectMemberService, never()).deleteProjectScrumMaster(anyLong());
  }

  @Test
  void update_ShouldReplaceScrumMaster_WhenScrumMasterUsernameProvided() {
    // Arrange
    ProjectUpdateRequestDto updateDto =
        ProjectUpdateRequestDto.builder().scrumMasterUsername("newscrummaster").build();

    when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
    when(projectRepository.save(testProject)).thenReturn(testProject);
    when(projectMapper.mapToMetaDataResponseDto(testProject))
        .thenReturn(projectMetaDataResponseDto);

    // Act
    ProjectMetaDataResponseDto result = projectService.update(1L, updateDto);

    // Assert
    assertNotNull(result);
    verify(projectMemberService).deleteProjectScrumMaster(1L);
    verify(projectMemberService).saveAny(testProject, "newscrummaster", Role.SCRUM_MASTER);
    verify(projectRepository).save(testProject);
  }

  @Test
  void update_ShouldUpdateAllFields_WhenAllFieldsProvided() {
    // Arrange
    when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
    when(projectRepository.save(testProject)).thenReturn(testProject);
    when(projectMapper.mapToMetaDataResponseDto(testProject))
        .thenReturn(projectMetaDataResponseDto);

    // Act
    ProjectMetaDataResponseDto result = projectService.update(1L, projectUpdateRequestDto);

    // Assert
    assertNotNull(result);
    assertEquals("Updated Project", testProject.getName());
    assertEquals("Updated Description", testProject.getDescription());
    verify(projectMemberService).deleteProjectScrumMaster(1L);
    verify(projectMemberService).saveAny(testProject, "newscrummaster", Role.SCRUM_MASTER);
  }

  @Test
  void update_ShouldThrowNotFoundException_WhenProjectDoesNotExist() {
    // Arrange
    when(projectRepository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> projectService.update(1L, projectUpdateRequestDto));
  }

  @Test
  void getProjects_ShouldReturnAllProjects() {
    // Arrange
    Project project2 =
        Project.builder()
            .id(2L)
            .name("Project 2")
            .description("Description 2")
            .creationDate(LocalDateTime.now())
            .build();

    List<Project> projects = Arrays.asList(testProject, project2);

    ProjectMetaDataResponseDto responseDto2 =
        ProjectMetaDataResponseDto.builder()
            .id(2L)
            .name("Project 2")
            .description("Description 2")
            .build();

    when(projectRepository.findAll()).thenReturn(projects);
    when(projectMapper.mapToMetaDataResponseDto(testProject))
        .thenReturn(projectMetaDataResponseDto);
    when(projectMapper.mapToMetaDataResponseDto(project2)).thenReturn(responseDto2);

    // Act
    List<ProjectMetaDataResponseDto> result = projectService.getProjects();

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(projectRepository).findAll();
    verify(projectMapper, times(2)).mapToMetaDataResponseDto(any(Project.class));
  }

  @Test
  void getProjects_ShouldReturnEmptyList_WhenNoProjectsExist() {
    // Arrange
    when(projectRepository.findAll()).thenReturn(Arrays.asList());

    // Act
    List<ProjectMetaDataResponseDto> result = projectService.getProjects();

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(projectRepository).findAll();
  }

  @Test
  void getProject_ShouldReturnProject_WhenProjectExists() {
    // Arrange
    when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
    when(projectMapper.mapToResponseDto(testProject)).thenReturn(projectResponseDto);

    // Act
    ProjectResponseDto result = projectService.getProject(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Test Project", result.getName());
    verify(projectRepository).findById(1L);
    verify(projectMapper).mapToResponseDto(testProject);
  }

  @Test
  void getProject_ShouldThrowNotFoundException_WhenProjectDoesNotExist() {
    // Arrange
    when(projectRepository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> projectService.getProject(1L));
  }

  @Test
  void findById_ShouldReturnProject_WhenProjectExists() {
    // Arrange
    when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

    // Act
    Project result = projectService.findById(1L);

    // Assert
    assertNotNull(result);
    assertEquals(testProject.getId(), result.getId());
    assertEquals(testProject.getName(), result.getName());
    verify(projectRepository).findById(1L);
  }

  @Test
  void findById_ShouldThrowNotFoundException_WhenProjectDoesNotExist() {
    // Arrange
    when(projectRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> projectService.findById(999L));

    assertEquals("no project found with the given id", exception.getMessage());
    verify(projectRepository).findById(999L);
  }

  @Test
  void deleteProject_ShouldDeleteProject() {
    // Arrange
    doNothing().when(projectRepository).deleteById(1L);

    // Act
    projectService.deleteProject(1L);

    // Assert
    verify(projectRepository).deleteById(1L);
  }

  @Test
  void saveMember_ShouldSaveMemberToProject() {
    // Arrange
    ProjectMemberRequestDto memberRequestDto =
        ProjectMemberRequestDto.builder().username("newmember").role(Role.DEVELOPER).build();

    when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
    doNothing().when(projectMemberService).save(testProject, memberRequestDto);

    // Act
    projectService.saveMember(1L, memberRequestDto);

    // Assert
    verify(projectRepository).findById(1L);
    verify(projectMemberService).save(testProject, memberRequestDto);
  }

  @Test
  void saveMember_ShouldThrowNotFoundException_WhenProjectDoesNotExist() {
    // Arrange
    ProjectMemberRequestDto memberRequestDto =
        ProjectMemberRequestDto.builder().username("newmember").role(Role.DEVELOPER).build();

    when(projectRepository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> projectService.saveMember(1L, memberRequestDto));
  }

  @Test
  void addRoleToMember_ShouldAddRoleToExistingMember() {
    // Arrange
    ProjectMember projectMember =
        ProjectMember.builder()
            .id(1L)
            .user(testUser)
            .project(testProject)
            .userRole(Role.DEVELOPER)
            .build();

    List<ProjectMember> projectMembers = Arrays.asList(projectMember);

    when(projectMemberService.findAllByProjectIdAndUsername(1L, "testuser"))
        .thenReturn(projectMembers);

    User authenticatedUser = User.builder().id(1L).username("testuser").build();

    UserContext authUserContext = new UserContext(authenticatedUser);
    authUserContext.setProject(testProject);

    when(userAuthorizationService.getAuthenticatedUser()).thenReturn(authUserContext);

    // Act
    projectService.addRoleToMember(1L, "testuser", Role.TESTER);

    // Assert
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "testuser");
    verify(projectMemberService).save(eq(testProject), any(ProjectMemberRequestDto.class));
  }

  @Test
  void addRoleToMember_ShouldThrowNotFoundException_WhenMemberDoesNotExist() {
    // Arrange
    when(projectMemberService.findAllByProjectIdAndUsername(1L, "nonexistent"))
        .thenReturn(Arrays.asList());

    // Act & Assert
    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> projectService.addRoleToMember(1L, "nonexistent", Role.TESTER));

    assertEquals(
        "the project does not have any member with the given username", exception.getMessage());
  }

  @Test
  void removeRoleFromMember_ShouldRemoveSpecificRole() {
    // Arrange
    ProjectMember projectMember =
        ProjectMember.builder()
            .id(1L)
            .user(testUser)
            .project(testProject)
            .userRole(Role.DEVELOPER)
            .build();

    List<ProjectMember> projectMembers = Arrays.asList(projectMember);

    when(projectMemberService.findAllByProjectIdAndUsername(1L, "testuser"))
        .thenReturn(projectMembers);
    doNothing().when(projectMemberService).deleteRelation(1L);

    // Act
    projectService.removeRoleFromMember(1L, "testuser", Role.DEVELOPER);

    // Assert
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "testuser");
    verify(projectMemberService).deleteRelation(1L);
  }

  @Test
  void removeRoleFromMember_ShouldThrowNotFoundException_WhenRoleDoesNotExist() {
    // Arrange
    ProjectMember projectMember =
        ProjectMember.builder()
            .id(1L)
            .user(testUser)
            .project(testProject)
            .userRole(Role.DEVELOPER)
            .build();

    List<ProjectMember> projectMembers = Arrays.asList(projectMember);

    when(projectMemberService.findAllByProjectIdAndUsername(1L, "testuser"))
        .thenReturn(projectMembers);

    // Act & Assert
    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> projectService.removeRoleFromMember(1L, "testuser", Role.TESTER));

    assertEquals(
        "the project does not have any member with the given username and role",
        exception.getMessage());
  }

  @Test
  void removeRoleFromMember_ShouldThrowNotFoundException_WhenMemberDoesNotExist() {
    // Arrange
    when(projectMemberService.findAllByProjectIdAndUsername(1L, "nonexistent"))
        .thenReturn(Arrays.asList());

    // Act & Assert
    assertThrows(
        NotFoundException.class,
        () -> projectService.removeRoleFromMember(1L, "nonexistent", Role.DEVELOPER));
  }

  @Test
  void removeMember_ShouldRemoveAllMemberRelations() {
    // Arrange
    ProjectMember member1 =
        ProjectMember.builder()
            .id(1L)
            .user(testUser)
            .project(testProject)
            .userRole(Role.DEVELOPER)
            .build();

    ProjectMember member2 =
        ProjectMember.builder()
            .id(2L)
            .user(testUser)
            .project(testProject)
            .userRole(Role.TESTER)
            .build();

    List<ProjectMember> projectMembers = Arrays.asList(member1, member2);

    when(projectMemberService.findAllByProjectIdAndUsername(1L, "testuser"))
        .thenReturn(projectMembers);
    doNothing().when(projectMemberService).deleteRelation(anyLong());

    // Act
    projectService.removeMember(1L, "testuser");

    // Assert
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "testuser");
    verify(projectMemberService).deleteRelation(1L);
    verify(projectMemberService).deleteRelation(2L);
  }

  @Test
  void removeMember_ShouldHandleEmptyMemberList() {
    // Arrange
    when(projectMemberService.findAllByProjectIdAndUsername(1L, "testuser"))
        .thenReturn(Arrays.asList());

    // Act
    projectService.removeMember(1L, "testuser");

    // Assert
    verify(projectMemberService).findAllByProjectIdAndUsername(1L, "testuser");
    verify(projectMemberService, never()).deleteRelation(anyLong());
  }

  @Test
  void getProjectMembers_ShouldReturnAllMembers_WhenRoleIsNull() {
    // Arrange
    ProjectMember member1 =
        ProjectMember.builder()
            .id(1L)
            .user(testUser)
            .project(testProject)
            .userRole(Role.DEVELOPER)
            .build();

    ProjectMember member2 =
        ProjectMember.builder()
            .id(2L)
            .user(scrumMasterUser)
            .project(testProject)
            .userRole(Role.TESTER)
            .build();

    List<ProjectMember> projectMembers = Arrays.asList(member1, member2);

    ProjectMemberResponseDto responseDto1 = new ProjectMemberResponseDto();
    ProjectMemberResponseDto responseDto2 = new ProjectMemberResponseDto();

    when(projectMemberService.findAllByProjectId(1L)).thenReturn(projectMembers);
    when(projectMemberMapper.mapToProjectMemberResponseDto(member1)).thenReturn(responseDto1);
    when(projectMemberMapper.mapToProjectMemberResponseDto(member2)).thenReturn(responseDto2);

    // Act
    List<ProjectMemberResponseDto> result = projectService.getProjectMembers(1L, null);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(projectMemberService).findAllByProjectId(1L);
    verify(projectMemberService, never()).findAllByProjectIdAndUserRole(anyLong(), any());
  }

  @Test
  void getProjectMembers_ShouldReturnFilteredMembers_WhenRoleIsProvided() {
    // Arrange
    ProjectMember projectMember =
        ProjectMember.builder()
            .id(1L)
            .user(testUser)
            .project(testProject)
            .userRole(Role.DEVELOPER)
            .build();

    List<ProjectMember> projectMembers = Arrays.asList(projectMember);
    ProjectMemberResponseDto responseDto = new ProjectMemberResponseDto();

    when(projectMemberService.findAllByProjectIdAndUserRole(1L, Role.DEVELOPER))
        .thenReturn(projectMembers);
    when(projectMemberMapper.mapToProjectMemberResponseDto(projectMember)).thenReturn(responseDto);

    // Act
    List<ProjectMemberResponseDto> result = projectService.getProjectMembers(1L, Role.DEVELOPER);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(projectMemberService).findAllByProjectIdAndUserRole(1L, Role.DEVELOPER);
    verify(projectMemberService, never()).findAllByProjectId(anyLong());
  }

  @Test
  void getProjectMembers_ShouldReturnEmptyList_WhenNoMembersExist() {
    // Arrange
    when(projectMemberService.findAllByProjectId(1L)).thenReturn(Arrays.asList());

    // Act
    List<ProjectMemberResponseDto> result = projectService.getProjectMembers(1L, null);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
