package com.sia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.dto.AdDTO;
import com.sia.entity.AdStatus;
import com.sia.exception.GlobalExceptionHandler;
import com.sia.service.AdService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdController.class)
@Import(GlobalExceptionHandler.class)
class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdService adService;

    private AdDTO buildAdDto() {
        return AdDTO.builder()
                .id(1)
                .title("iPhone 13")
                .description("отличное состояние")
                .price(new BigDecimal("1200.00"))
                .city("Minsk")
                .viewsCount(10)
                .createdAt(LocalDateTime.now())
                .status(AdStatus.ACTIVE)
                .userId(1)
                .categoryId(1)
                .build();
    }

    @Test
    @DisplayName("POST /api/ads - создать объявление")
    void createAd_shouldReturnCreatedAd() throws Exception {
        AdDTO request = buildAdDto();
        request.setId(null);

        AdDTO response = buildAdDto();

        when(adService.createAd(any(AdDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("iPhone 13"))
                .andExpect(jsonPath("$.description").value("отличное состояние"))
                .andExpect(jsonPath("$.price").value(1200.00))
                .andExpect(jsonPath("$.city").value("Minsk"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.categoryId").value(1));

        verify(adService).createAd(any(AdDTO.class));
    }

    @Test
    @DisplayName("POST /api/ads - ошибка валидации")
    void createAd_shouldReturnBadRequest_whenInvalidBody() throws Exception {
        AdDTO request = buildAdDto();
        request.setTitle("");
        request.setPrice(new BigDecimal("-1"));

        mockMvc.perform(post("/api/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists())
                .andExpect(jsonPath("$.errors.price").exists());
    }

    @Test
    @DisplayName("GET /api/ads/{id} - получить объявление по id")
    void getById_shouldReturnAd() throws Exception {
        AdDTO response = buildAdDto();

        when(adService.getAdById(1)).thenReturn(response);

        mockMvc.perform(get("/api/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("iPhone 13"));

        verify(adService).getAdById(1);
    }

    @Test
    @DisplayName("GET /api/ads/{id} - ошибка валидации id")
    void getById_shouldReturnBadRequest_whenIdInvalid() throws Exception {
        mockMvc.perform(get("/api/ads/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/ads - получить страницу объявлений")
    void getAll_shouldReturnPage() throws Exception {
        AdDTO ad = buildAdDto();

        when(adService.getAllAds(any())).thenReturn(
                new PageImpl<>(List.of(ad), PageRequest.of(0, 10), 1)
        );

        mockMvc.perform(get("/api/ads?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("iPhone 13"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /api/ads/search - поиск объявлений")
    void search_shouldReturnPage() throws Exception {
        AdDTO ad = buildAdDto();

        when(adService.searchByTitle(eq("iPhone"), any())).thenReturn(
                new PageImpl<>(List.of(ad), PageRequest.of(0, 10), 1)
        );

        mockMvc.perform(get("/api/ads/search")
                        .param("keyword", "iPhone")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("iPhone 13"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(adService).searchByTitle(eq("iPhone"), any());
    }

    @Test
    @DisplayName("GET /api/ads/search - пустой keyword")
    void search_shouldReturnBadRequest_whenKeywordBlank() throws Exception {
        mockMvc.perform(get("/api/ads/search")
                        .param("keyword", " "))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/ads/{id} - обновить объявление")
    void updateAd_shouldReturnUpdatedAd() throws Exception {
        AdDTO request = buildAdDto();
        request.setTitle("iPhone 13 Pro");

        AdDTO response = buildAdDto();
        response.setTitle("iPhone 13 Pro");

        when(adService.updateAd(eq(1), any(AdDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("iPhone 13 Pro"));

        verify(adService).updateAd(eq(1), any(AdDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/ads/{id} - удалить объявление")
    void deleteAd_shouldReturnOk() throws Exception {
        doNothing().when(adService).deleteAd(1);

        mockMvc.perform(delete("/api/ads/1"))
                .andExpect(status().isOk());

        verify(adService).deleteAd(1);
    }
}
