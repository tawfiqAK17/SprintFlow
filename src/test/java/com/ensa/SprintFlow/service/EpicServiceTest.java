package com.ensa.SprintFlow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
  private UserStory userStory1;
  private UserStory userStory2;

  @BeforeEach
  void setUp() {
    // Setup Project
    project = Project.builder().id(1L).name("Test Project").description("Test Description").build();

    // Setup Default Epic
    defaultEpic =
        Epic.builder()
            .id(999L)
            .title("Default Epic")
            .description("Default Description")
            .project(project)
            .userStories(new ArrayList<>())
            .build();

    project.setDefaultEpic(defaultEpic);

    // Setup Epic
    epic =
        Epic.builder()
            .id(1L)
            .title("Epic Title")
            .description("Epic Description")
            .project(project)
            .userStories(new ArrayList<>())
            .build();

    // Setup UserStories
    userStory1 = UserStory.builder().id(1L).title("User Story 1").epic(epic).build();

    userStory2 = UserStory.builder().id(2L).title("User Story 2").epic(epic).build();

    // Setup DTOs
    epicRequestDto =
        EpicRequestDto.builder().title("New Epic").description("New Description").build();

    epicResponseDto =
        EpicResponseDto.builder()
            .id(1L)
            .title("Epic Title")
            .description("Epic Description")
            .userStoriesCount(0)
            .build();
  }

  @Test
  void save_ShouldReturnSavedEpic() {
    // Arrange
    when(epicRepository.save(epic)).thenReturn(epic);

    // Act
    Epic result = epicService.save(epic);

    // Assert
    assertNotNull(result);
    assertEquals(epic.getId(), result.getId());
    verify(epicRepository, times(1)).save(epic);
  }

  @Test
  void getAllEpics_ShouldReturnListOfEpicResponseDto() {
    // Arrange
    List<Epic> epics = Arrays.asList(epic);
    when(epicRepository.findAllByProjectId(1L)).thenReturn(epics);
    when(mapper.mapToEpicResponseDto(epic)).thenReturn(epicResponseDto);

    // Act
    List<EpicResponseDto> result = epicService.getAllEpics(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(epicResponseDto.getId(), result.get(0).getId());
    verify(epicRepository, times(1)).findAllByProjectId(1L);
    verify(mapper, times(1)).mapToEpicResponseDto(epic);
  }

  @Test
  void getEpic_WithValidId_ShouldReturnEpicResponseDto() {
    // Arrange
    when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
    when(mapper.mapToEpicResponseDto(epic)).thenReturn(epicResponseDto);

    // Act
    EpicResponseDto result = epicService.getEpic(1L);

    // Assert
    assertNotNull(result);
    assertEquals(epicResponseDto.getId(), result.getId());
    verify(epicRepository, times(1)).findById(1L);
    verify(mapper, times(1)).mapToEpicResponseDto(epic);
  }

  @Test
  void getEpic_WithInvalidId_ShouldThrowNotFoundException() {
    // Arrange
    when(epicRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> epicService.getEpic(999L));
    assertEquals("no epic found with the given id", exception.getMessage());
    verify(epicRepository, times(1)).findById(999L);
  }

  @Test
  void createEpic_ShouldReturnCreatedEpicResponseDto() {
    // Arrange
    when(mapper.mapToEpic(project, epicRequestDto)).thenReturn(epic);
    when(epicRepository.save(epic)).thenReturn(epic);
    when(mapper.mapToEpicResponseDto(epic)).thenReturn(epicResponseDto);

    // Act
    EpicResponseDto result = epicService.createEpic(project, epicRequestDto);

    // Assert
    assertNotNull(result);
    assertEquals(epicResponseDto.getId(), result.getId());
    verify(mapper, times(1)).mapToEpic(project, epicRequestDto);
    verify(epicRepository, times(1)).save(epic);
    verify(mapper, times(1)).mapToEpicResponseDto(epic);
  }

  @Test
  void update_WithValidId_ShouldUpdateAndReturnEpicResponseDto() {
    // Arrange
    EpicRequestDto updateDto =
        EpicRequestDto.builder().title("Updated Title").description("Updated Description").build();

    when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
    when(epicRepository.save(any(Epic.class))).thenReturn(epic);
    when(mapper.mapToEpicResponseDto(epic)).thenReturn(epicResponseDto);

    // Act
    EpicResponseDto result = epicService.update(1L, updateDto);

    // Assert
    assertNotNull(result);
    assertEquals("Updated Title", epic.getTitle());
    assertEquals("Updated Description", epic.getDescription());
    verify(epicRepository, times(1)).findById(1L);
    verify(epicRepository, times(1)).save(epic);
    verify(mapper, times(1)).mapToEpicResponseDto(epic);
  }

  @Test
  void update_WithPartialData_ShouldUpdateOnlyProvidedFields() {
    // Arrange
    EpicRequestDto updateDto = EpicRequestDto.builder().title("Updated Title Only").build();

    String originalDescription = epic.getDescription();
    when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
    when(epicRepository.save(any(Epic.class))).thenReturn(epic);
    when(mapper.mapToEpicResponseDto(epic)).thenReturn(epicResponseDto);

    // Act
    EpicResponseDto result = epicService.update(1L, updateDto);

    // Assert
    assertNotNull(result);
    assertEquals("Updated Title Only", epic.getTitle());
    assertEquals(originalDescription, epic.getDescription());
    verify(epicRepository, times(1)).save(epic);
  }

  @Test
  void update_WithInvalidId_ShouldThrowNotFoundException() {
    // Arrange
    when(epicRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> epicService.update(999L, epicRequestDto));
    assertEquals("there is no epic with the given id", exception.getMessage());
    verify(epicRepository, times(1)).findById(999L);
    verify(epicRepository, never()).save(any());
  }

  @Test
  void delete_WithValidId_ShouldMoveUserStoriesToDefaultEpicAndDeleteEpic() {
    // Arrange
    epic.getUserStories().add(userStory1);
    epic.getUserStories().add(userStory2);

    when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
    when(userAuthorizationService.getContextProject()).thenReturn(project);

    // Act
    epicService.delete(1L);

    // Assert
    assertEquals(defaultEpic, userStory1.getEpic());
    assertEquals(defaultEpic, userStory2.getEpic());
    assertTrue(epic.getUserStories().isEmpty());
    assertEquals(2, defaultEpic.getUserStories().size());
    verify(epicRepository, times(1)).findById(1L);
    verify(epicRepository, times(1)).delete(epic);
    verify(userAuthorizationService, times(1)).getContextProject();
  }

  @Test
  void delete_WithInvalidId_ShouldThrowNotFoundException() {
    // Arrange
    when(epicRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> epicService.delete(999L));
    assertEquals("there is no epic with the given id", exception.getMessage());
    verify(epicRepository, times(1)).findById(999L);
    verify(epicRepository, never()).delete(any());
  }

  @Test
  void addUserStories_WithInvalidEpicId_ShouldThrowNotFoundException() {
    // Arrange
    when(epicRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception =
        assertThrows(
            NotFoundException.class, () -> epicService.addUserStories(999L, Arrays.asList(1L, 2L)));
    assertEquals("there is no epic with the given id", exception.getMessage());
    verify(epicRepository, times(1)).findById(999L);
  }

  @Test
  void removeUserStory_WithValidIds_ShouldRemoveUserStoryFromEpic() {
    // Arrange
    epic.getUserStories().add(userStory1);
    epic.getUserStories().add(userStory2);

    when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

    // Act
    epicService.removeUserStory(1L, 1L);

    // Assert
    verify(epicRepository, times(1)).findById(1L);
  }

  @Test
  void removeUserStory_WithInvalidEpicId_ShouldThrowNotFoundException() {
    // Arrange
    when(epicRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> epicService.removeUserStory(999L, 1L));
    assertEquals("there is no sprint with the given id", exception.getMessage());
    verify(epicRepository, times(1)).findById(999L);
  }

  @Test
  void findById_WithValidId_ShouldReturnEpic() {
    // Arrange
    when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

    // Act
    Epic result = epicService.findById(1L);

    // Assert
    assertNotNull(result);
    assertEquals(epic.getId(), result.getId());
    verify(epicRepository, times(1)).findById(1L);
  }

  @Test
  void findById_WithInvalidId_ShouldThrowNotFoundException() {
    // Arrange
    when(epicRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception =
        assertThrows(NotFoundException.class, () -> epicService.findById(999L));
    assertEquals("there is no epic with the given name", exception.getMessage());
    verify(epicRepository, times(1)).findById(999L);
  }
}
