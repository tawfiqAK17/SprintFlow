package com.ensa.SprintFlow.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.SprintFlow.dto.epic.request.EpicRequestDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// providing each test class with it own database
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:epic-test"})
public class EpicControllerTest extends ControllerTest {

  @Nested
  class EpicCreationTest {
    @Test
    public void testEpicCreationWithValidInput() throws JacksonException, Exception {
      EpicRequestDto creationDto =
          EpicRequestDto.builder().title("test epic").description("an epic for testing").build();
      mockMvc
          .perform(
              post("/projects/" + testProject.getId() + "/epics")
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(creationDto)))
          .andExpect(status().isCreated());
    }

    @Test
    public void testEpicCreationWithOutTitle() throws JacksonException, Exception {
      EpicRequestDto creationDto =
          EpicRequestDto.builder().description("an epic for testing").build();
      mockMvc
          .perform(
              post("/projects/" + testProject.getId() + "/epics")
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(creationDto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details", Matchers.containsInAnyOrder("title is required")));
    }

    @Test
    public void testEpicCreationWithOutDescription() throws JacksonException, Exception {
      EpicRequestDto creationDto = EpicRequestDto.builder().title("test epic").build();
      mockMvc
          .perform(
              post("/projects/" + testProject.getId() + "/epics")
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(creationDto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details", Matchers.containsInAnyOrder("description is required")));
    }

    @Test
    public void testEpicCreationWithOutDescriptionAndTitle() throws JacksonException, Exception {
      EpicRequestDto creationDto = EpicRequestDto.builder().build();
      mockMvc
          .perform(
              post("/projects/" + testProject.getId() + "/epics")
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(creationDto)))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("the fields are not valid"))
          .andExpect(
              MockMvcResultMatchers.jsonPath(
                  "$.details",
                  Matchers.containsInAnyOrder("description is required", "title is required")));
    }
  }

  @Nested
  public class EpicUpdateTest {
    @Test
    public void testUpdateAllEpicFields() throws JacksonException, Exception {
      EpicRequestDto requestDto =
          EpicRequestDto.builder()
              .title("updated epic")
              .description("this epic was updated")
              .build();
      mockMvc
          .perform(
              put("/projects/" + testProject.getId() + "/epics/" + testEpic.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(requestDto)))
          .andExpect(status().isOk());
    }

    @Test
    public void testUpdateEpicTitle() throws JacksonException, Exception {
      EpicRequestDto requestDto = EpicRequestDto.builder().title("updated epic").build();
      mockMvc
          .perform(
              put("/projects/" + testProject.getId() + "/epics/" + testEpic.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(requestDto)))
          .andExpect(status().isOk());
    }

    @Test
    public void testUpdateEpicDescription() throws JacksonException, Exception {
      EpicRequestDto requestDto =
          EpicRequestDto.builder().description("this epic was updated").build();
      mockMvc
          .perform(
              put("/projects/" + testProject.getId() + "/epics/" + testEpic.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(requestDto)))
          .andExpect(status().isOk());
    }
  }

  @Nested
  class EpicDeletionTest {
    @Test
    public void testEpicDeletion() throws Exception {
      mockMvc
          .perform(
              delete("/projects/" + testProject.getId() + "/epics/" + testEpic.getId())
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }
  }
}
