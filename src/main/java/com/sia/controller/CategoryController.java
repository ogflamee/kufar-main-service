package com.sia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.sia.dto.CategoryDTO;
import com.sia.service.CategoryService;

import java.util.List;

/**
 * REST-контроллер для работы с категориями объявлений.
 * предоставляет endpoints для создания, получения, обновления категорий.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryDTO create(@Valid @RequestBody CategoryDTO dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/{id}")
    public CategoryDTO updateCategory(@PathVariable @Positive Integer id,
                                      @Valid @RequestBody CategoryDTO dto) {
        return categoryService.updateCategory(id, dto);
    }
}
