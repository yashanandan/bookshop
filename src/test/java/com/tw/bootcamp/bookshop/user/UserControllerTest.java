package com.tw.bootcamp.bookshop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserWhenCredentialsAreValid() throws Exception {
        String email = "testemail@test.com";
        CreateUserCommand userCredentials = new CreateUserCommand(email, "foobar");
        User user = new UserTestBuilder().withEmail(email).build();
        when(userService.create(userCredentials)).thenReturn(user);
        UserView userView = UserView.builder().id(user.getId().toString()).email(email).build();

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userCredentials))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(userView)));

        verify(userService, times(1)).create(userCredentials);
    }

    @Test
    void shouldRespondWithErrorMessageWhenCreateUserFails() throws Exception {
        CreateUserCommand userCredentials = new CreateUserCommand("testemail@test.com", "foobar");
        when(userService.create(userCredentials)).thenThrow(new InvalidEmailException());

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userCredentials))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User with same email already created"));
    }
}