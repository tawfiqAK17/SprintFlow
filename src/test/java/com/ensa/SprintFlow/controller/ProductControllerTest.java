package com.ensa.SprintFlow.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.request.ProjectUpdateRequestDto;
import com.ensa.SprintFlow.dto.response.ProjectMetaDataResponseDto;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.core.JacksonException;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:project-test"})
public class ProductControllerTest extends ControllerTest {

  @Nested
  @Order(1)
  class ProjectCreationTest {

    @Test
    public void testProjectCreationWithValidInput() throws Exception {
      ProjectRequestDto projectRequestDto = new ProjectRequestDto();
      projectRequestDto.setName("testProject");
      projectRequestDto.setDescription("a project to test the end point POST /projects");
      projectRequestDto.setScrumMasterUsername("scrumMasterTest");

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
      ProjectRequestDto projectRequestDto = new ProjectRequestDto();
      projectRequestDto.setName("testProject");
      projectRequestDto.setDescription("a project to test the end point POST /projects");

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
      ProjectRequestDto projectRequestDto = new ProjectRequestDto();
      projectRequestDto.setDescription("a project to test the end point POST /projects");
      projectRequestDto.setScrumMasterUsername("scrumMasterTest");

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
      ProjectRequestDto projectRequestDto = new ProjectRequestDto();
      projectRequestDto.setName("testProject");
      projectRequestDto.setScrumMasterUsername("scrumMasterTest");

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
  class ProjectUpdateAndDeleteTest {
    private Long projectId;

    @BeforeAll
    public void createProject() throws JacksonException, Exception {
      ProjectRequestDto projectDto = new ProjectRequestDto();
      projectDto.setName("test project");
      projectDto.setDescription("a project to test the project update endpoint");
      projectDto.setScrumMasterUsername("scrumMasterTest");
      MvcResult result =
          mockMvc
              .perform(
                  post("/projects")
                      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(projectDto)))
              .andExpect(status().isCreated())
              .andReturn();
      String resultAsString = result.getResponse().getContentAsString();
      ProjectMetaDataResponseDto projectMetaDate =
          objectMapper.readValue(resultAsString, ProjectMetaDataResponseDto.class);
      this.projectId = projectMetaDate.getId();
    }

    @Test
    public void testProjectUpdateWithValidInput() throws JacksonException, Exception {
      ProjectUpdateRequestDto projectDto = new ProjectUpdateRequestDto();
      String name = "updated test project";
      String description = "updated description of a project to test the project update endpoint";
      projectDto.setName(name);
      projectDto.setDescription(description);

      mockMvc
          .perform(
              put("/projects/" + this.projectId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(projectDto)))
          .andExpect(status().isCreated())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(projectId))
          .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
          .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
    }

    @Test
    @Order(2)
    public void testChangingProjectScrumMaster() throws JacksonException, Exception {
      ProjectUpdateRequestDto projectDto = new ProjectUpdateRequestDto();
      projectDto.setScrumMasterUsername("userTest");
      mockMvc
          .perform(
              put("/projects/" + this.projectId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(projectDto)))
          .andExpect(status().isCreated())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(projectId))
          .andExpect(MockMvcResultMatchers.jsonPath("$.scrumMaster.username").value("userTest"));
    }

    @Test
    public void testProjectDeletion() throws Exception {
      mockMvc
          .perform(
              delete("/projects/" + this.projectId)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
          .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletionOfNoneExistingProject() throws Exception {
      mockMvc
          .perform(delete("/projects/6969").header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
          .andExpect(
              status()
                  .isUnauthorized()); // unauthorized because the user considered as not a member of
      // the project
    }
  }
}
