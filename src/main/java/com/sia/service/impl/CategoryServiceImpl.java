package com.sia.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sia.dto.CategoryDTO;
import com.sia.entity.Category;
import com.sia.mapper.CategoryMapper;
import com.sia.repository.CategoryRepository;
import com.sia.service.CategoryService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .toList();
    }
}
