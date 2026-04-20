package com.sia.service;

import com.sia.dto.CategoryDTO;

import java.util.List;

/**
 * Сервис для работы с категориями.
 * содержит бизнес-логику создания, обновления
 *  и получения объявлений.
 */
public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO dto);

    List<CategoryDTO> getAllCategories();

    CategoryDTO updateCategory(Integer id, CategoryDTO dto);
}
