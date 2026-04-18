package com.sia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.dto.FavoriteDTO;
import com.sia.exception.GlobalExceptionHandler;
import com.sia.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
@Import(GlobalExceptionHandler.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavoriteService favoriteService;

    private FavoriteDTO buildFavoriteDto() {
        return FavoriteDTO.builder()
                .id(1)
                .userId(1)
                .adId(2)
                .addedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/favorites - добавить в избранное")
    void add_shouldReturnCreatedFavorite() throws Exception {
        FavoriteDTO request = FavoriteDTO.builder()
                .userId(1)
                .adId(2)
                .build();

        FavoriteDTO response = buildFavoriteDto();

        when(favoriteService.addToFavorite(any(FavoriteDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.adId").value(2));

        verify(favoriteService).addToFavorite(any(FavoriteDTO.class));
    }

    @Test
    @DisplayName("POST /api/favorites - ошибка валидации")
    void add_shouldReturnBadRequest_whenInvalidBody() throws Exception {
        FavoriteDTO request = FavoriteDTO.builder()
                .userId(0)
                .adId(null)
                .build();

        mockMvc.perform(post("/api/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.userId").exists())
                .andExpect(jsonPath("$.errors.adId").exists());
    }

    @Test
    @DisplayName("GET /api/favorites/user/{userId} - получить избранное пользователя")
    void getFavorites_shouldReturnPage() throws Exception {
        FavoriteDTO favorite = buildFavoriteDto();

        when(favoriteService.getUserFavorites(eq(1), any())).thenReturn(
                new PageImpl<>(List.of(favorite), PageRequest.of(0, 10), 1)
        );

        mockMvc.perform(get("/api/favorites/user/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].userId").value(1))
                                .andExpect(jsonPath("$.content[0].adId").value(2))
                                .andExpect(jsonPath("$.totalElements").value(1));

        verify(favoriteService).getUserFavorites(eq(1), any());
    }

    @Test
    @DisplayName("GET /api/favorites/user/{userId} - невалидный userId")
    void getFavorites_shouldReturnBadRequest_whenInvalidUserId() throws Exception {
        mockMvc.perform(get("/api/favorites/user/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/favorites - удалить из избранного")
    void remove_shouldReturnOk() throws Exception {
        doNothing().when(favoriteService).removeFromFavorites(1, 2);

        mockMvc.perform(delete("/api/favorites")
                        .param("userId", "1")
                        .param("adId", "2"))
                .andExpect(status().isOk());

        verify(favoriteService).removeFromFavorites(1, 2);
    }

    @Test
    @DisplayName("DELETE /api/favorites - невалидные параметры")
    void remove_shouldReturnBadRequest_whenParamsInvalid() throws Exception {
        mockMvc.perform(delete("/api/favorites")
                        .param("userId", "0")
                        .param("adId", "-1"))
                .andExpect(status().isBadRequest());
    }
}