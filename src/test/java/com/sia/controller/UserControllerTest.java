package com.sia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.config.TestSecurityConfig;
import com.sia.dto.UserCreateDTO;
import com.sia.dto.UserDTO;
import com.sia.exception.GlobalExceptionHandler;
import com.sia.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserCreateDTO buildCreateDTO() {
        return UserCreateDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .password("123456")
                .build();
    }

    private UserDTO buildUserDTO() {
        return UserDTO.builder()
                .id(1)
                .email("test@gmail.com")
                .username("test")
                .phoneNumber("+375291234567")
                .rating(4.5)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/users - оздать пользователя")
    void createUser_shouldReturnCreateUser() throws Exception {
        UserCreateDTO request = buildCreateDTO();
        UserDTO response = buildUserDTO();

        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.username").value("test"));

        verify(userService).createUser(any(UserCreateDTO.class));
    }

    @Test
    @DisplayName("POST /api/users - ошибка валидации")
    void createUser_shouldReturnBadRequest_whenInvalidBody() throws Exception {

        UserCreateDTO request = UserCreateDTO.builder()
                .email("wrong_email")
                .username("ab")
                .password("123")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    @DisplayName("GET /api/users/{id} - получить пользователя")
    void getUser_shouldReturnUser() throws Exception {
        when(userService.getUserById(1)).thenReturn(buildUserDTO());
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test"));

        verify(userService).getUserById(1);
    }

    @Test
    @DisplayName("GET /api/users - получить всех пользователей")
    void getAllUsers_shouldReturnList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(buildUserDTO()));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("test@gmail.com"));

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("PUT /api/users/{id} - обновить пользователя")
    void updateUser_shouldReturnUpdatedUser() throws Exception {

        UserCreateDTO request = UserCreateDTO.builder()
                .email("new@gmail.com")
                .username("new_user")
                .password("123456")
                .build();

        UserDTO response = UserDTO.builder()
                .id(1)
                .email("new@gmail.com")
                .username("new_user")
                .phoneNumber("+375291112233")
                .rating(4.5)
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.updateUser(eq(1), any(UserCreateDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.username").value("new_user"));

        verify(userService).updateUser(eq(1), any(UserCreateDTO.class));

    }

    @Test
    @DisplayName("DELETE /api/users/{id} - удалить пользователя")
    void deleteUser_shouldReturnOk() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService).deleteUser(1);

    }

    @Test
    @DisplayName("GET /api/users/{id} - невалидный id")
    void getUser_shouldReturnBadRequest_whenInvalidId() throws Exception {
        mockMvc.perform(get("/api/users/0"))
                .andExpect(status().isBadRequest());
    }
}