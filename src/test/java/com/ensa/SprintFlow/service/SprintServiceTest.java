package com.ensa.SprintFlow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.SprintFlow.dto.sprint.request.SprintRequestDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintResponseDto;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.SprintMapper;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.Sprint;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.SprintRepository;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SprintServiceTest {

  @Mock private SprintRepository sprintRepository;

  @Mock private SprintMapper sprintMapper;

  @Mock private UserStoryService userStoryService;

  @Mock private UserAuthorizationService userAuthorizationService;

  @InjectMocks private SprintService sprintService;

  private SprintRequestDto sprintRequestDto;
  private Sprint sprint;
  private SprintResponseDto sprintResponseDto;
  private UserContext userContext;
  private Project project;

  @BeforeEach
  void setUp() {
    // Setup common test data
    sprintRequestDto =
        SprintRequestDto.builder()
            .title("Sprint 1")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(14))
            .build();

    project = Project.builder().id(1L).name("Test Project").sprints(new ArrayList<>()).build();

    sprint =
        Sprint.builder()
            .id(1L)
            .title("Sprint 1")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(14))
            .project(project)
            .userStories(new ArrayList<>())
            .build();

    sprintResponseDto =
        SprintResponseDto.builder()
            .id(1L)
            .title("Sprint 1")
            .startDate(sprint.getStartDate())
            .endDate(sprint.getEndDate())
            .userStoriesCount(0)
            .build();

    User user = new User();
    userContext = new UserContext(user.toEntityUser());
  }

  @Test
  void save_ShouldSaveSprintSuccessfully() {
    // Given
    when(sprintMapper.mapToSprint(sprintRequestDto)).thenReturn(sprint);
    when(userAuthorizationService.getAuthenticatedUser()).thenReturn(userContext);
    when(sprintRepository.save(sprint)).thenReturn(sprint);
    when(sprintMapper.maptoSprintResponseDto(sprint)).thenReturn(sprintResponseDto);

    // When
    SprintResponseDto result = sprintService.save(sprintRequestDto);

    // Then
    assertNotNull(result);
    assertEquals(sprintResponseDto.getId(), result.getId());
    assertEquals(sprintResponseDto.getTitle(), result.getTitle());
    verify(sprintMapper).mapToSprint(sprintRequestDto);
    verify(userAuthorizationService).getAuthenticatedUser();
    verify(sprintRepository).save(sprint);
    verify(sprintMapper).maptoSprintResponseDto(sprint);
  }

 @Test
  void getSprint_WhenSprintExists_ShouldReturnSprintResponseDto() {
    // Given
    Long sprintId = 1L;
    when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
    when(sprintMapper.maptoSprintResponseDto(sprint)).thenReturn(sprintResponseDto);

    // When
    SprintResponseDto result = sprintService.getSprint(sprintId);

    // Then
    assertNotNull(result);
    assertEquals(sprintResponseDto.getId(), result.getId());
    assertEquals(sprintResponseDto.getTitle(), result.getTitle());
    verify(sprintRepository).findById(sprintId);
    verify(sprintMapper).maptoSprintResponseDto(sprint);
  }

  @Test
  void getSprint_WhenSprintDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long sprintId = 999L;
    when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> sprintService.getSprint(sprintId));

    assertEquals("the is no sprint with the given id", exception.getMessage());
    verify(sprintRepository).findById(sprintId);
    verify(sprintMapper, never()).maptoSprintResponseDto(any());
  }

  @Test
  void update_WithAllFields_ShouldUpdateSprintSuccessfully() {
    // Given
    Long sprintId = 1L;
    SprintRequestDto updateDto =
        SprintRequestDto.builder()
            .title("Updated Sprint")
            .startDate(LocalDateTime.now().plusDays(1))
            .endDate(LocalDateTime.now().plusDays(15))
            .build();

    Sprint updatedSprint =
        Sprint.builder()
            .id(1L)
            .title("Updated Sprint")
            .startDate(updateDto.getStartDate())
            .endDate(updateDto.getEndDate())
            .userStories(new ArrayList<>())
            .build();

    SprintResponseDto updatedResponseDto =
        SprintResponseDto.builder()
            .id(1L)
            .title("Updated Sprint")
            .startDate(updateDto.getStartDate())
            .endDate(updateDto.getEndDate())
            .userStoriesCount(0)
            .build();

    when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
    when(sprintRepository.save(sprint)).thenReturn(updatedSprint);
    when(sprintMapper.maptoSprintResponseDto(updatedSprint)).thenReturn(updatedResponseDto);

    // When
    SprintResponseDto result = sprintService.update(sprintId, updateDto);

    // Then
    assertNotNull(result);
    assertEquals("Updated Sprint", result.getTitle());
    verify(sprintRepository).findById(sprintId);
    verify(sprintRepository).save(sprint);
  }

  @Test
  void update_WithPartialFields_ShouldUpdateOnlyProvidedFields() {
    // Given
    Long sprintId = 1L;
    SprintRequestDto updateDto = SprintRequestDto.builder().title("Partial Update").build();

    when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
    when(sprintRepository.save(sprint)).thenReturn(sprint);
    when(sprintMapper.maptoSprintResponseDto(sprint)).thenReturn(sprintResponseDto);

    // When
    SprintResponseDto result = sprintService.update(sprintId, updateDto);

    // Then
    assertNotNull(result);
    verify(sprintRepository).findById(sprintId);
    verify(sprintRepository).save(sprint);
  }

  @Test
  void update_WhenSprintDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long sprintId = 999L;
    when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(
            NotFoundException.class, () -> sprintService.update(sprintId, sprintRequestDto));

    assertEquals("the is no sprint with the given id", exception.getMessage());
    verify(sprintRepository).findById(sprintId);
    verify(sprintRepository, never()).save(any());
  }

  @Test
  void addUserStories_ShouldAddUserStoriesToSprint() {
    // Given
    Long sprintId = 1L;
    List<Long> userStoryIds = Arrays.asList(1L, 2L, 3L);

    UserStory userStory1 = UserStory.builder().id(1L).title("User Story 1").build();
    UserStory userStory2 = UserStory.builder().id(2L).title("User Story 2").build();
    UserStory userStory3 = UserStory.builder().id(3L).title("User Story 3").build();

    List<UserStory> userStories = Arrays.asList(userStory1, userStory2, userStory3);
    List<UserStory> sprintUserStories = new ArrayList<>();
    sprint.setUserStories(sprintUserStories);

    when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
    when(userStoryService.findAllByIds(userStoryIds)).thenReturn(userStories);

    // When
    sprintService.addUserStories(sprintId, userStoryIds);

    // Then
    assertEquals(3, sprint.getUserStories().size());
    verify(sprintRepository).findById(sprintId);
    verify(userStoryService).findAllByIds(userStoryIds);
  }

  @Test
  void addUserStories_WhenSprintDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long sprintId = 999L;
    List<Long> userStoryIds = Arrays.asList(1L, 2L);

    when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(
            NotFoundException.class, () -> sprintService.addUserStories(sprintId, userStoryIds));

    assertEquals("there is no sprint with the given id", exception.getMessage());
    verify(sprintRepository).findById(sprintId);
    verify(userStoryService, never()).findAllByIds(any());
  }

  @Test
  void removeUserStory_WhenSprintExists_ShouldNotRemoveUserStory() {
    // Given - This test documents the BUG in the current implementation
    Long sprintId = 1L;
    Long userStoryId = 1L;

    UserStory userStory1 = UserStory.builder().id(1L).title("User Story 1").build();
    UserStory userStory2 = UserStory.builder().id(2L).title("User Story 2").build();

    List<UserStory> sprintUserStories = new ArrayList<>(Arrays.asList(userStory1, userStory2));
    sprint.setUserStories(sprintUserStories);

    when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));

    // When
    sprintService.removeUserStory(sprintId, userStoryId);

    assertEquals(1, sprint.getUserStories().size());
    verify(sprintRepository).findById(sprintId);
  }

  @Test
  void removeUserStory_WhenSprintDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long sprintId = 999L;
    Long userStoryId = 1L;

    when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(
            NotFoundException.class, () -> sprintService.removeUserStory(sprintId, userStoryId));

    assertEquals("there is no sprint with the given id", exception.getMessage());
    verify(sprintRepository).findById(sprintId);
  }

  @Test
  void delete_ShouldDeleteSprint() {
    // Given
    Long sprintId = 1L;
    doNothing().when(sprintRepository).deleteById(sprintId);

    // When
    sprintService.delete(sprintId);

    // Then
    verify(sprintRepository).deleteById(sprintId);
  }

  @Test
  void findById_WhenSprintExists_ShouldReturnSprint() {
    // Given
    Long sprintId = 1L;
    when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));

    // When
    Sprint result = sprintService.findById(sprintId);

    // Then
    assertNotNull(result);
    assertEquals(sprint.getId(), result.getId());
    assertEquals(sprint.getTitle(), result.getTitle());
    verify(sprintRepository).findById(sprintId);
  }

  @Test
  void findById_WhenSprintDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long sprintId = 999L;
    when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> sprintService.findById(sprintId));

    assertEquals("there is no sprint with the given id", exception.getMessage());
    verify(sprintRepository).findById(sprintId);
  }

  // Mock User class for testing
  static class User {
    private Long id = 1L;
    private String username = "testuser";
    private String email = "test@example.com";

    public Long getId() {
      return id;
    }

    public String getUsername() {
      return username;
    }

    public String getEmail() {
      return email;
    }

    public com.ensa.SprintFlow.model.User toEntityUser() {
      com.ensa.SprintFlow.model.User entityUser = new com.ensa.SprintFlow.model.User();
      entityUser.setUsername(username);
      entityUser.setEmail(email);
      entityUser.setId(id);
      return entityUser;
    }
  }
}
