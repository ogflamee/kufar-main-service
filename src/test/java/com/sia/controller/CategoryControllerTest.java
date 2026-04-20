package com.sia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.config.TestSecurityConfig;
import com.sia.dto.CategoryDTO;
import com.sia.exception.GlobalExceptionHandler;
import com.sia.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private CategoryDTO buildCategoryDto() {
        return CategoryDTO.builder()
                .id(1)
                .name("Electronics")
                .build();
    }

    @Test
    @DisplayName("POST /api/categories - создать категорию")
    void create_shouldReturnCreatedCategory() throws Exception {
        CategoryDTO request = CategoryDTO.builder().name("Electronics").build();
        CategoryDTO response = buildCategoryDto();

        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryService).createCategory(any(CategoryDTO.class));
    }

    @Test
    @DisplayName("POST /api/categories - ошибка валидации")
    void create_shouldReturnBadRequest_whenInvalidBody() throws Exception {
        CategoryDTO request = CategoryDTO.builder().name("").build();

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    @DisplayName("GET /api/categories - получить все категории")
    void getAll_shouldReturnList() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(buildCategoryDto()));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Electronics"));

        verify(categoryService).getAllCategories();
    }

    @Test
    @DisplayName("PUT /api/categories/{id} - обновить категорию")
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        CategoryDTO request = CategoryDTO.builder().name("Phones").build();
        CategoryDTO response = CategoryDTO.builder().id(1).name("Phones").build();

        when(categoryService.updateCategory(eq(1), any(CategoryDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Phones"));

        verify(categoryService).updateCategory(eq(1), any(CategoryDTO.class));
    }

    @Test
    @DisplayName("PUT /api/categories/{id} - невалидный id")
    void updateCategory_shouldReturnBadRequest_whenIdInvalid() throws Exception {
        CategoryDTO request = CategoryDTO.builder().name("Phones").build();

        mockMvc.perform(put("/api/categories/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}