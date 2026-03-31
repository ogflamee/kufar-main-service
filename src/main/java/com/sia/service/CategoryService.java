package com.sia.service;

import com.sia.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO dto);

    List<CategoryDTO> getAllCategories();
}
