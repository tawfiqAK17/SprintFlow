package com.ensa.SprintFlow.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.SprintFlow.dto.sprint.request.SprintRequestDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintResponseDto;
import com.ensa.SprintFlow.model.Sprint;
import com.ensa.SprintFlow.service.SprintService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:sprint-test"})
class SprintControllerTest extends ControllerTest {

  @Autowired private SprintService sprintService;

  private Sprint testSprint;

  @BeforeAll
  @Override
  public void setUp() throws Exception {
    super.setUp();
    loginAsScrumMaster();
    createTestSprint();
  }

  private void createTestSprint() throws Exception {
    loginAsScrumMaster();

    SprintRequestDto dto =
        SprintRequestDto.builder()
            .title("Test Sprint 1")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(14))
            .build();

    MvcResult result =
        mockMvc
            .perform(
                post("/projects/{projectId}/sprints", testProject.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            // .andExpect(status().isCreated())
            .andReturn();

    String responseBodyAsString = result.getResponse().getContentAsString();
    SprintResponseDto responseDto =
        objectMapper.readValue(responseBodyAsString, SprintResponseDto.class);

    testSprint =
        Sprint.builder()
            .id(responseDto.getId())
            .title(responseDto.getTitle())
            .startDate(responseDto.getStartDate())
            .endDate(responseDto.getEndDate())
            .project(testProject)
            .build();
  }

  @Nested
  @Order(1)
  class SprintCreationTest {
    @Test
    public void testSprintCreationWithValidInput() throws Exception {

      loginAsScrumMaster();
      SprintRequestDto dto =
          SprintRequestDto.builder()
              .title("New Sprint")
              .startDate(LocalDateTime.now().plusDays(1))
              .endDate(LocalDateTime.now().plusDays(15))
              .build();

      mockMvc
          .perform(
              post("/projects/{projectId}/sprints", testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isCreated())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Sprint"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.userStoriesCount").value(0));
    }

    @Test
    public void testSprintCreationWithoutTitle() throws Exception {
      loginAsScrumMaster();
      SprintRequestDto dto =
          SprintRequestDto.builder()
              .startDate(LocalDateTime.now())
              .endDate(LocalDateTime.now().plusDays(14))
              .build();

      mockMvc
          .perform(
              post("/projects/{projectId}/sprints", testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details", Matchers.containsInAnyOrder("the title is required")));
    }

    @Test
    public void testSprintCreationWithoutStartDate() throws Exception {
      loginAsScrumMaster();
      SprintRequestDto dto =
          SprintRequestDto.builder()
              .title("Sprint without start date")
              .endDate(LocalDateTime.now().plusDays(14))
              .build();

      mockMvc
          .perform(
              post("/projects/{projectId}/sprints", testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details", Matchers.containsInAnyOrder("the start date is required")));
    }

    @Test
    public void testSprintCreationWithoutEndDate() throws Exception {
      loginAsScrumMaster();
      SprintRequestDto dto =
          SprintRequestDto.builder()
              .title("Sprint without end date")
              .startDate(LocalDateTime.now())
              .build();

      mockMvc
          .perform(
              post("/projects/{projectId}/sprints", testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details", Matchers.containsInAnyOrder("the end date is required")));
    }

    @Test
    public void testSprintCreationAsProductOwner() throws Exception {
      loginAsProductOwner();
      SprintRequestDto dto =
          SprintRequestDto.builder()
              .title("New Sprint")
              .startDate(LocalDateTime.now().plusDays(1))
              .endDate(LocalDateTime.now().plusDays(15))
              .build();

      mockMvc
          .perform(
              post("/projects/{projectId}/sprints", testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isUnauthorized())
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.message")
                  .value("the user should be a SCRUM_MASTER"));
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class SprintRetrievalTest {
    @Test
    public void testGetAllSprintsAsProductOwner() throws Exception {
      loginAsProductOwner();
      LocalDateTime startDate = LocalDateTime.now().minusDays(1);
      LocalDateTime endDate = LocalDateTime.now().plusDays(30);

      mockMvc
          .perform(
              get("/projects/{projectId}/sprints", testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .param("startDate", startDate.toString())
                  .param("endDate", endDate.toString())
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].startDate").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].endDate").exists());
    }

    @Test
    public void testGetAllSprintsWithoutAuthentication() throws Exception {
      LocalDateTime startDate = LocalDateTime.now().minusDays(1);
      LocalDateTime endDate = LocalDateTime.now().plusDays(30);

      mockMvc
          .perform(
              get("/projects/{projectId}/sprints", testProject.getId())
                  .param("startDate", startDate.toString())
                  .param("endDate", endDate.toString())
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isForbidden());
    }

    @Test
    public void testGetSprintAsProductOwner() throws Exception {
      loginAsProductOwner();

      mockMvc
          .perform(
              get(
                      "/projects/{projectId}/sprints/{sprintId}",
                      testProject.getId(),
                      testSprint.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testSprint.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(testSprint.getTitle()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.userStoriesCount").exists());
    }

    @Test
    public void testGetSprintWithNonExistentId() throws Exception {
      loginAsProductOwner();
      Long nonExistentId = 99999L;

      mockMvc
          .perform(
              get("/projects/{projectId}/sprints/{sprintId}", testProject.getId(), nonExistentId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class SprintUpdateTest {
    @Test
    public void testSprintUpdateWithValidInput() throws Exception {
      loginAsScrumMaster();
      SprintRequestDto updateDto =
          SprintRequestDto.builder()
              .title("Updated Sprint")
              .startDate(LocalDateTime.now().plusDays(2))
              .endDate(LocalDateTime.now().plusDays(16))
              .build();

      mockMvc
          .perform(
              put(
                      "/projects/{projectId}/sprints/{sprintId}",
                      testProject.getId(),
                      testSprint.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(updateDto)))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testSprint.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Sprint"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").exists());
    }

    @Test
    public void testSprintUpdateAsProductOwner() throws Exception {
      loginAsProductOwner();
      SprintRequestDto updateDto =
          SprintRequestDto.builder()
              .title("Updated Sprint")
              .startDate(LocalDateTime.now().plusDays(2))
              .endDate(LocalDateTime.now().plusDays(16))
              .build();

      mockMvc
          .perform(
              put(
                      "/projects/{projectId}/sprints/{sprintId}",
                      testProject.getId(),
                      testSprint.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(updateDto)))
          .andExpect(status().isUnauthorized())
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.message")
                  .value("the user should be a SCRUM_MASTER"));
    }

    @Test
    public void testSprintUpdateWithNonExistentId() throws Exception {
      loginAsScrumMaster();
      Long nonExistentId = 99999L;
      SprintRequestDto updateDto =
          SprintRequestDto.builder()
              .title("Updated Sprint")
              .startDate(LocalDateTime.now().plusDays(2))
              .endDate(LocalDateTime.now().plusDays(16))
              .build();

      mockMvc
          .perform(
              put("/projects/{projectId}/sprints/{sprintId}", testProject.getId(), nonExistentId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(updateDto)))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  class SprintDeletionTest {
    @Test
    public void testSprintDeletion() throws Exception {
      loginAsScrumMaster();

      mockMvc
          .perform(
              delete(
                      "/projects/{projectId}/sprints/{sprintId}",
                      testProject.getId(),
                      testSprint.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }

    @Test
    public void testDeletionOfNonExistingSprint() throws Exception {
      loginAsScrumMaster();
      Long nonExistentId = 99999L;

      mockMvc
          .perform(
              delete("/projects/{projectId}/sprints/{sprintId}", testProject.getId(), nonExistentId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletionOfSprintAsProductOwner() throws Exception {
      loginAsProductOwner();

      mockMvc
          .perform(
              delete(
                      "/projects/{projectId}/sprints/{sprintId}",
                      testProject.getId(),
                      testSprint.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isUnauthorized())
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.message")
                  .value("the user should be a SCRUM_MASTER"));
    }
  }

  @Nested
  class SprintUserStoriesManagementTest {
    @Test
    public void testAddUserStoriesToSprint() throws Exception {
      loginAsScrumMaster();
      List<Long> userStoryIds = Arrays.asList(1L, 2L, 3L);

      mockMvc
          .perform(
              post(
                      "/projects/{projectId}/sprints/{sprintId}/user_stories",
                      testProject.getId(),
                      testSprint.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(userStoryIds)))
          .andExpect(status().isOk());
    }

    @Test
    public void testAddUserStoriesAsProductOwner() throws Exception {
      loginAsProductOwner();
      List<Long> userStoryIds = Arrays.asList(1L, 2L, 3L);

      mockMvc
          .perform(
              post(
                      "/projects/{projectId}/sprints/{sprintId}/user_stories",
                      testProject.getId(),
                      testSprint.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(userStoryIds)))
          .andExpect(status().isUnauthorized())
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.message")
                  .value("the user should be a SCRUM_MASTER"));
    }

    @Test
    public void testAddUserStoriesToNonExistentSprint() throws Exception {
      loginAsScrumMaster();
      Long nonExistentId = 99999L;
      List<Long> userStoryIds = Arrays.asList(1L, 2L, 3L);

      mockMvc
          .perform(
              post(
                      "/projects/{projectId}/sprints/{sprintId}/user_stories",
                      testProject.getId(),
                      nonExistentId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(userStoryIds)))
          .andExpect(status().isNotFound());
    }

    @Test
    public void testRemoveUserStoryFromSprint() throws Exception {
      loginAsScrumMaster();
      Long userStoryId = 1L;

      mockMvc
          .perform(
              delete(
                      "/projects/{projectId}/sprints/{sprintId}/user_stories/{userStoryId}",
                      testProject.getId(),
                      testSprint.getId(),
                      userStoryId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }

    @Test
    public void testRemoveUserStoryAsProductOwner() throws Exception {
      loginAsProductOwner();
      Long userStoryId = 1L;

      mockMvc
          .perform(
              delete(
                      "/projects/{projectId}/sprints/{sprintId}/user_stories/{userStoryId}",
                      testProject.getId(),
                      testSprint.getId(),
                      userStoryId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isUnauthorized())
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.message")
                  .value("the user should be a SCRUM_MASTER"));
    }

    @Test
    public void testRemoveUserStoryFromNonExistentSprint() throws Exception {
      loginAsScrumMaster();
      Long nonExistentSprintId = 99999L;
      Long userStoryId = 1L;

      mockMvc
          .perform(
              delete(
                      "/projects/{projectId}/sprints/{sprintId}/user_stories/{userStoryId}",
                      testProject.getId(),
                      nonExistentSprintId,
                      userStoryId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
  }
}
