package com.horvath.usernotesapp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horvath.usernotesapp.api.dto.UserDto;
import com.horvath.usernotesapp.domain.User;
import com.horvath.usernotesapp.service.UserService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    private final MockMvc mockMvc;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final Flyway flyway;

    private static final String BASE_URL = "/web/api/user";

    @Autowired
    public UserControllerTest(MockMvc mockMvc,
                              UserService userService,
                              ObjectMapper objectMapper,
                              Flyway flyway) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.flyway = flyway;
    }

    @BeforeEach
    void resetDatabase() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("New User");

        mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("New User")));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        Long userId = createUserAndGetId();

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("Updated User");

        mockMvc.perform(put(BASE_URL + "/" + userId + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name", is("Updated User")));
    }

    @Test
    void deleteUser_shouldSoftDeleteUser() throws Exception {
        Long userId = createUserAndGetId();

        mockMvc.perform(delete(BASE_URL + "/" + userId + "/delete"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_shouldReturnNotFound() throws Exception {
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("Updated Name");

        mockMvc.perform(put(BASE_URL + "/9999/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/9999/delete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_shouldReturnBadRequest() throws Exception {
        UserDto invalidDto = new UserDto();

        mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    private Long createUserAndGetId() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        User user = userService.createUser(userDto);
        return user.getId();
    }
}
