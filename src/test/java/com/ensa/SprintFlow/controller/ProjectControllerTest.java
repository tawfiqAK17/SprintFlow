package com.ensa.SprintFlow.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.request.ProjectUpdateRequestDto;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.core.JacksonException;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:project-test"})
public class ProjectControllerTest extends ControllerTest {

  @Nested
  @Order(1)
  class ProjectCreationTest {
    @Test
    public void testProjectCreationWithValidInput() throws Exception {
      ProjectRequestDto projectRequestDto =
          ProjectRequestDto.builder()
              .name("testProject")
              .description("a project to test the end point POST /projects")
              .scrumMasterUsername("scrumMasterTest")
              .build();

      mockMvc
          .perform(
              post("/projects")
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(projectRequestDto)))
          .andExpect(status().isCreated());
    }

    @Test
    public void testProjectCreationWithOutScrumMaster() throws Exception {
      ProjectRequestDto projectRequestDto =
          ProjectRequestDto.builder()
              .name("testProject")
              .description("a project to test the end point POST /projects")
              .build();

      mockMvc
          .perform(
              post("/projects")
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(projectRequestDto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details", Matchers.containsInAnyOrder("scrum master username is required")));
    }

    @Test
    public void testProjectCreationWithName() throws Exception {
      ProjectRequestDto projectRequestDto =
          ProjectRequestDto.builder()
              .description("a project to test the end point POST /projects")
              .scrumMasterUsername("scrumMasterTest")
              .build();

      mockMvc
          .perform(
              post("/projects")
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(projectRequestDto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details", Matchers.containsInAnyOrder("name is required")));
    }

    @Test
    public void testProjectCreationWithOutDescription() throws Exception {
      ProjectRequestDto projectRequestDto =
          ProjectRequestDto.builder()
              .name("testProject")
              .scrumMasterUsername("scrumMasterTest")
              .build();

      mockMvc
          .perform(
              post("/projects")
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(projectRequestDto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details", Matchers.containsInAnyOrder("description is required")));
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class ProjectUpdateTest {
    @Test
    public void testProjectUpdateWithValidInput() throws JacksonException, Exception {
      String name = "updated test project";
      String description = "updated description of a project to test the project update endpoint";
      ProjectUpdateRequestDto projectDto =
          ProjectUpdateRequestDto.builder().name(name).description(description).build();
      mockMvc
          .perform(
              put("/projects/" + testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(projectDto)))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testProject.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
          .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
    }

    @Test
    public void testChangingProjectScrumMaster() throws JacksonException, Exception {
      ProjectUpdateRequestDto projectDto =
          ProjectUpdateRequestDto.builder().scrumMasterUsername("userTest").build();
      mockMvc
          .perform(
              put("/projects/" + testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(projectDto)))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testProject.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.scrumMaster.username").value("userTest"));
    }
  }

  @Nested
  public class ProjectDeletionTest {
    @Test
    public void testProjectDeletion() throws Exception {
      mockMvc
          .perform(
              delete("/projects/" + testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
          .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletionOfNoneExistingProject() throws Exception {
      mockMvc
          .perform(delete("/projects/6969").header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
          .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletionOfProjectAsScrumMaster() throws Exception {
      loginAsScrumMaster();
      mockMvc
          .perform(
              delete("/projects/" + testProject.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
          .andExpect(status().isUnauthorized());
      loginAsProductOwner();
    }
  }
}
