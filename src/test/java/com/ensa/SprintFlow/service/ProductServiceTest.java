package com.ensa.SprintFlow.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.SprintFlow.dto.request.LoginRequestDto;
import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.response.LoginResponseDto;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.security.RegisterService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private RegisterService registerService;

  private String jwt;

  private void createUsers() {
    // Create product owner
    User productOwner = new User();
    productOwner.setFirstName("productOwner");
    productOwner.setLastName("test");
    productOwner.setUsername("productOwnerTest");
    productOwner.setEmail("productOwner@gmail.com");
    productOwner.setPassword("productOwnertestPassword");
    productOwner.setVerified(true);
    registerService.register(productOwner);

    // Create scrum master
    User scrumMaster = new User();
    scrumMaster.setFirstName("scrumMaster");
    scrumMaster.setLastName("test");
    scrumMaster.setUsername("scrumMasterTest");
    scrumMaster.setEmail("scrumMaster@gmail.com");
    scrumMaster.setPassword("scrumMasterPassword");
    scrumMaster.setVerified(true);
    registerService.register(scrumMaster);
  }

  @BeforeAll
  public void loginAndGetJwtToken() throws Exception {
    createUsers();
    LoginRequestDto dto = new LoginRequestDto();
    dto.setUsername("productOwnerTest");
    dto.setPassword("productOwnertestPassword");

    MvcResult result =
        mockMvc
            .perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andReturn();

    String responseBodyAsString = result.getResponse().getContentAsString();
    LoginResponseDto responseDto =
        objectMapper.readValue(responseBodyAsString, LoginResponseDto.class);
    this.jwt = responseDto.getJwt();
  }

  @Nested
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
}
