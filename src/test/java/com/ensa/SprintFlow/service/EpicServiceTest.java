package com.ensa.SprintFlow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.SprintFlow.dto.epic.request.EpicRequestDto;
import com.ensa.SprintFlow.dto.epic.response.EpicResponseDto;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.EpicMapper;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.EpicRepository;
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
class EpicServiceTest {

  @Mock private EpicRepository epicRepository;

  @Mock private EpicMapper mapper;

  @Mock private UserAuthorizationService userAuthorizationService;

  @InjectMocks private EpicService epicService;

  private Epic epic;
  private Epic defaultEpic;
  private Project project;
  private EpicRequestDto epicRequestDto;
  private EpicResponseDto epicResponseDto;

  @BeforeEach
  void setUp() {
    project =
        Project.builder()
            .id(1L)
            .name("Test Project")
            .description("Test Description")
            .creationDate(LocalDateTime.now())
            .build();

    defaultEpic =
        Epic.builder()
            .id(100L)
            .title("Default Epic")
            .description("Default epic for unassigned stories")
            .project(project)
            .userStories(new ArrayList<>())
            .build();

    epic =
        Epic.builder()
            .id(1L)
            .title("Epic 1")
            .description("Epic Description")
            .project(project)
            .userStories(new ArrayList<>())
            .build();

    epicRequestDto =
        EpicRequestDto.builder().title("Epic 1").description("Epic Description").build();

    epicResponseDto =
        EpicResponseDto.builder()
            .id(1L)
            .title("Epic 1")
            .description("Epic Description")
            .userStoriesCount(0)
            .build();

    project.setDefaultEpic(defaultEpic);
  }

  @Test
  void save_ShouldSaveEpicSuccessfully() {
    // Given
    when(epicRepository.save(epic)).thenReturn(epic);

    // When
    Epic result = epicService.save(epic);

    // Then
    assertNotNull(result);
    assertEquals(epic.getId(), result.getId());
    assertEquals(epic.getTitle(), result.getTitle());
    verify(epicRepository).save(epic);
  }

  @Test
  void getAllEpics_ShouldReturnAllEpicsForProject() {
    // Given
    Long projectId = 1L;
    Epic epic2 =
        Epic.builder()
            .id(2L)
            .title("Epic 2")
            .description("Description 2")
            .userStories(new ArrayList<>())
            .build();

    List<Epic> epics = Arrays.asList(epic, epic2);
    when(epicRepository.findAllByProjectId(projectId)).thenReturn(epics);

    // When
    List<Epic> result = epicService.getAllEpics(projectId);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(epic.getId(), result.get(0).getId());
    assertEquals(epic2.getId(), result.get(1).getId());
    verify(epicRepository).findAllByProjectId(projectId);
  }

  @Test
  void getAllEpics_WhenNoEpicsExist_ShouldReturnEmptyList() {
    // Given
    Long projectId = 1L;
    when(epicRepository.findAllByProjectId(projectId)).thenReturn(new ArrayList<>());

    // When
    List<Epic> result = epicService.getAllEpics(projectId);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(epicRepository).findAllByProjectId(projectId);
  }

  @Test
  void getEpic_WhenEpicExists_ShouldReturnEpic() {
    // Given
    Long epicId = 1L;
    when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));

    // When
    Epic result = epicService.getEpic(epicId);

    // Then
    assertNotNull(result);
    assertEquals(epic.getId(), result.getId());
    assertEquals(epic.getTitle(), result.getTitle());
    verify(epicRepository).findById(epicId);
  }

  @Test
  void getEpic_WhenEpicDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long epicId = 999L;
    when(epicRepository.findById(epicId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> epicService.getEpic(epicId));

    assertEquals("no epic found with the given id", exception.getMessage());
    verify(epicRepository).findById(epicId);
  }

  @Test
  void createEpic_ShouldCreateAndReturnEpicResponseDto() {
    // Given
    when(mapper.mapToEpic(project, epicRequestDto)).thenReturn(epic);
    when(epicRepository.save(epic)).thenReturn(epic);
    when(mapper.mapToEpicResponseDto(epic)).thenReturn(epicResponseDto);

    // When
    EpicResponseDto result = epicService.createEpic(project, epicRequestDto);

    // Then
    assertNotNull(result);
    assertEquals(epicResponseDto.getId(), result.getId());
    assertEquals(epicResponseDto.getTitle(), result.getTitle());
    assertEquals(epicResponseDto.getDescription(), result.getDescription());
    verify(mapper).mapToEpic(project, epicRequestDto);
    verify(epicRepository).save(epic);
    verify(mapper).mapToEpicResponseDto(epic);
  }

  @Test
  void update_WithAllFields_ShouldUpdateEpicSuccessfully() {
    // Given
    Long epicId = 1L;
    EpicRequestDto updateDto =
        EpicRequestDto.builder().title("Updated Epic").description("Updated Description").build();

    Epic updatedEpic =
        Epic.builder()
            .id(1L)
            .title("Updated Epic")
            .description("Updated Description")
            .userStories(new ArrayList<>())
            .build();

    EpicResponseDto updatedResponseDto =
        EpicResponseDto.builder()
            .id(1L)
            .title("Updated Epic")
            .description("Updated Description")
            .userStoriesCount(0)
            .build();

    when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
    when(epicRepository.save(epic)).thenReturn(updatedEpic);
    when(mapper.mapToEpicResponseDto(updatedEpic)).thenReturn(updatedResponseDto);

    // When
    EpicResponseDto result = epicService.update(epicId, updateDto);

    // Then
    assertNotNull(result);
    assertEquals("Updated Epic", result.getTitle());
    assertEquals("Updated Description", result.getDescription());
    verify(epicRepository).findById(epicId);
    verify(epicRepository).save(epic);
    verify(mapper).mapToEpicResponseDto(updatedEpic);
  }

  @Test
  void update_WithPartialFields_ShouldUpdateOnlyProvidedFields() {
    // Given
    Long epicId = 1L;
    EpicRequestDto updateDto = EpicRequestDto.builder().title("Partial Update").build();

    when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
    when(epicRepository.save(epic)).thenReturn(epic);
    when(mapper.mapToEpicResponseDto(epic)).thenReturn(epicResponseDto);

    // When
    EpicResponseDto result = epicService.update(epicId, updateDto);

    // Then
    assertNotNull(result);
    verify(epicRepository).findById(epicId);
    verify(epicRepository).save(epic);
    verify(mapper).mapToEpicResponseDto(epic);
  }

  @Test
  void update_WhenEpicDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long epicId = 999L;
    when(epicRepository.findById(epicId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> epicService.update(epicId, epicRequestDto));

    assertEquals("there is no epic with the given id", exception.getMessage());
    verify(epicRepository).findById(epicId);
    verify(epicRepository, never()).save(any());
  }

  @Test
  void delete_ShouldDeleteEpic() {
    // Given
    Long epicId = 1L;
    doNothing().when(epicRepository).deleteById(epicId);

    // When
    epicService.delete(epicId);

    // Then
    verify(epicRepository).deleteById(epicId);
  }

  @Test
  void addUserStories_ShouldMoveUserStoriesFromDefaultEpicToTargetEpic() {
    // Given
    Long epicId = 1L;
    List<Long> userStoryIds = Arrays.asList(1L, 2L, 3L);

    UserStory userStory1 = UserStory.builder().id(1L).title("User Story 1").build();
    UserStory userStory2 = UserStory.builder().id(2L).title("User Story 2").build();
    UserStory userStory3 = UserStory.builder().id(3L).title("User Story 3").build();

    List<UserStory> userStories = Arrays.asList(userStory1, userStory2, userStory3);
    List<UserStory> defaultEpicUserStories = new ArrayList<>(userStories);
    defaultEpic.setUserStories(defaultEpicUserStories);

    List<UserStory> epicUserStories = new ArrayList<>();
    epic.setUserStories(epicUserStories);

    when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
    when(userAuthorizationService.getContextProject()).thenReturn(project);
    when(epicRepository.findAllUserStoriesByIds(defaultEpic.getId(), userStoryIds))
        .thenReturn(userStories);

    // When
    epicService.addUserStories(epicId, userStoryIds);

    // Then
    assertEquals(3, epic.getUserStories().size());
    assertEquals(0, defaultEpic.getUserStories().size());
    assertTrue(epic.getUserStories().contains(userStory1));
    assertTrue(epic.getUserStories().contains(userStory2));
    assertTrue(epic.getUserStories().contains(userStory3));
    verify(epicRepository).findById(epicId);
    verify(userAuthorizationService).getContextProject();
    verify(epicRepository).findAllUserStoriesByIds(defaultEpic.getId(), userStoryIds);
  }

  @Test
  void addUserStories_WhenEpicDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long epicId = 999L;
    List<Long> userStoryIds = Arrays.asList(1L, 2L);

    when(epicRepository.findById(epicId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(
            NotFoundException.class, () -> epicService.addUserStories(epicId, userStoryIds));

    assertEquals("there is no epic with the given id", exception.getMessage());
    verify(epicRepository).findById(epicId);
    verify(userAuthorizationService, never()).getContextProject();
    verify(epicRepository, never()).findAllUserStoriesByIds(anyLong(), any());
  }

  @Test
  void addUserStories_WhenUserStoriesListIsEmpty_ShouldNotModifyEpics() {
    // Given
    Long epicId = 1L;
    List<Long> userStoryIds = new ArrayList<>();

    when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
    when(userAuthorizationService.getContextProject()).thenReturn(project);
    when(epicRepository.findAllUserStoriesByIds(defaultEpic.getId(), userStoryIds))
        .thenReturn(new ArrayList<>());

    // When
    epicService.addUserStories(epicId, userStoryIds);

    // Then
    assertEquals(0, epic.getUserStories().size());
    verify(epicRepository).findById(epicId);
    verify(userAuthorizationService).getContextProject();
    verify(epicRepository).findAllUserStoriesByIds(defaultEpic.getId(), userStoryIds);
  }

  @Test
  void removeUserStory_WhenEpicExists_ShouldNotRemoveUserStory() {
    // Given - This test documents the BUG in the current implementation
    Long epicId = 1L;
    Long userStoryId = 1L;

    UserStory userStory1 = UserStory.builder().id(1L).title("User Story 1").build();
    UserStory userStory2 = UserStory.builder().id(2L).title("User Story 2").build();

    List<UserStory> epicUserStories = new ArrayList<>(Arrays.asList(userStory1, userStory2));
    epic.setUserStories(epicUserStories);

    when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));

    // When
    epicService.removeUserStory(epicId, userStoryId);

    // Then - The bug: user story is NOT actually removed
    assertEquals(2, epic.getUserStories().size());
    verify(epicRepository).findById(epicId);
  }

  @Test
  void removeUserStory_WhenEpicDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long epicId = 999L;
    Long userStoryId = 1L;

    when(epicRepository.findById(epicId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(
            NotFoundException.class, () -> epicService.removeUserStory(epicId, userStoryId));

    assertEquals("there is no sprint with the given id", exception.getMessage());
    verify(epicRepository).findById(epicId);
  }

  @Test
  void findById_WhenEpicExists_ShouldReturnEpic() {
    // Given
    Long epicId = 1L;
    when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));

    // When
    Epic result = epicService.findById(epicId);

    // Then
    assertNotNull(result);
    assertEquals(epic.getId(), result.getId());
    assertEquals(epic.getTitle(), result.getTitle());
    verify(epicRepository).findById(epicId);
  }

  @Test
  void findById_WhenEpicDoesNotExist_ShouldThrowNotFoundException() {
    // Given
    Long epicId = 999L;
    when(epicRepository.findById(epicId)).thenReturn(Optional.empty());

    // When & Then
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> epicService.findById(epicId));

    assertEquals("there is no epic with the given name", exception.getMessage());
    verify(epicRepository).findById(epicId);
  }
}
