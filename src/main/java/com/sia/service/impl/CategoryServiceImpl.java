package com.sia.service.impl;

import com.sia.exception.ConflictException;
import com.sia.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.sia.dto.CategoryDTO;
import com.sia.entity.Category;
import com.sia.mapper.CategoryMapper;
import com.sia.repository.CategoryRepository;
import com.sia.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        log.info("creating category with name: {}", dto.getName());

        categoryRepository.findByName(dto.getName())
                .ifPresent(c ->{
                    throw new ConflictException("category already exists");
                } );

        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);

        log.info("category created with id: {}", saved.getId());

        return categoryMapper.toDTO(saved);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        log.info("fetching all categories");

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Integer id, CategoryDTO dto) {
        log.info("updating category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("category not found with id: {}", id);
                    return new NotFoundException("category was not found");
                });

        categoryRepository.findByName(dto.getName())
                .ifPresent(c ->{
                    throw new ConflictException("category already exists");
                } );

        category.setName(dto.getName());

        Category updated = categoryRepository.save(category);

        log.info("category updated with id: {}", id);

        return categoryMapper.toDTO(updated);
    }
}
