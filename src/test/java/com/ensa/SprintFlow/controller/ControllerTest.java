package com.ensa.SprintFlow.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.SprintFlow.security.dto.request.LoginRequestDto;
import com.ensa.SprintFlow.dto.project.request.ProjectRequestDto;
import com.ensa.SprintFlow.security.dto.response.LoginResponseDto;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.repository.UserRepository;
import com.ensa.SprintFlow.security.model.UserContext;
import com.ensa.SprintFlow.security.service.RegisterService;
import com.ensa.SprintFlow.service.EpicService;
import com.ensa.SprintFlow.service.ProjectService;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

abstract class ControllerTest {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @Autowired protected RegisterService registerService;

  @Autowired private ProjectService projectService;
  @Autowired private EpicService epicService;
  @Autowired private UserRepository userRepository;

  protected String jwt;
  protected Project testProject;
  protected Epic testEpic;

  @BeforeAll
  public void setUp() throws Exception {
    createUsers();
    loginAsProductOwner();
    setUpTestData();
  }

  protected void loginAsProductOwner() throws Exception {
    LoginRequestDto dto =
        LoginRequestDto.builder()
            .username("productOwnerTest")
            .password("productOwnerPassword")
            .build();

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

  protected void loginAsScrumMaster() throws Exception {
    LoginRequestDto dto =
        LoginRequestDto.builder()
            .username("scrumMasterTest")
            .password("scrumMasterPassword")
            .build();

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

  private void createUsers() {
    // Create product owner
    User productOwner =
        User.builder()
            .firstName("productOwner")
            .lastName("test")
            .username("productOwnerTest")
            .email("productOwner@gmail.com")
            .password("productOwnerPassword")
            .verified(true)
            .build();
    registerService.register(productOwner);

    // Create scrum master
    User scrumMaster =
        User.builder()
            .firstName("scrumMaster")
            .lastName("test")
            .username("scrumMasterTest")
            .email("scrumMaster@gmail.com")
            .password("scrumMasterPassword")
            .verified(true)
            .build();
    registerService.register(scrumMaster);

    // Create a user
    User user =
        User.builder()
            .firstName("user")
            .lastName("test")
            .username("userTest")
            .email("userTest@gmail.com")
            .password("userPasswordTest")
            .verified(true)
            .build();
    registerService.register(user);
  }

  private void setUpTestData() {
    testProject = createTestProject();
    testEpic = createTestEpic();
  }

  private Project createTestProject() {
    // mock the security context
    User productOwner = userRepository.findByUsername("productOwnerTest");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken(new UserContext(productOwner), null, null));
    SecurityContextHolder.setContext(securityContext);

    // create the project
    ProjectRequestDto dto =
        ProjectRequestDto.builder()
            .name("test project")
            .description("a project for testing the end points")
            .scrumMasterUsername("scrumMasterTest")
            .build();
    return projectService.save(dto);
  }

  private Epic createTestEpic() {
    Epic epic = Epic.builder().title("test epic").description("an epic for testing").build();
    epic.setProject(testProject);
    return epicService.save(epic);
  }
}
