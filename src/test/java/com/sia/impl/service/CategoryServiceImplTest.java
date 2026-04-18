package com.sia.impl.service;

import com.sia.dto.CategoryDTO;
import com.sia.entity.Category;
import com.sia.mapper.CategoryMapper;
import com.sia.repository.CategoryRepository;
import com.sia.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_success() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Electronics");

        Category category = new Category();
        Category savedCategory = new Category();
        savedCategory.setId(1);
        savedCategory.setName("Electronics");

        CategoryDTO response = new CategoryDTO();
        response.setId(1);
        response.setName("Electronics");

        when(categoryMapper.toEntity(dto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDTO(savedCategory)).thenReturn(response);

        CategoryDTO result = categoryService.createCategory(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Electronics", result.getName());

        verify(categoryMapper).toEntity(dto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDTO(savedCategory);
    }

    @Test
    void createCategory_alreadyExists() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Electronics");

        Category existing = new Category();
        existing.setId(1);
        existing.setName("Electronics");

        when(categoryRepository.findByName(dto.getName())).thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.createCategory(dto)
        );

        assertEquals("category already exists", ex.getMessage());

        verify(categoryRepository).findByName(dto.getName());
        verify(categoryRepository, never()).save(any());
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void getAllCategories_success() {
        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Cars");

        CategoryDTO dto1 = new CategoryDTO();
        dto1.setId(1);
        dto1.setName("Electronics");

        CategoryDTO dto2 = new CategoryDTO();
        dto2.setId(2);
        dto2.setName("Cars");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));
        when(categoryMapper.toDTO(category1)).thenReturn(dto1);
        when(categoryMapper.toDTO(category2)).thenReturn(dto2);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Cars", result.get(1).getName());

        verify(categoryRepository).findAll();
        verify(categoryMapper).toDTO(category1);
        verify(categoryMapper).toDTO(category2);
    }

    @Test
    void updateCategory_success() {
        Integer id = 1;

        CategoryDTO dto = new CategoryDTO();
        dto.setName("updated category");

        Category category = new Category();
        category.setId(id);
        category.setName("old category");

        Category updatedCategory = new Category();
        updatedCategory.setId(id);
        updatedCategory.setName("updated category");

        CategoryDTO response = new CategoryDTO();
        response.setId(id);
        response.setName("updated category");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toDTO(updatedCategory)).thenReturn(response);

        CategoryDTO result = categoryService.updateCategory(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("updated category", result.getName());
        assertEquals("updated category", category.getName());

        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDTO(updatedCategory);
    }

    @Test
    void updateCategory_notFound() {
        Integer id = 1;
        CategoryDTO dto = new CategoryDTO();
        dto.setName("updated category");

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> categoryService.updateCategory(id, dto)
        );

        assertEquals("category was not found", ex.getMessage());

        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).save(any());
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void updateCategory_alreadyExists() {
        Integer id = 1;

        CategoryDTO dto = new CategoryDTO();
        dto.setName("Electronics");

        Category current = new Category();
        current.setId(id);
        current.setName("Old name");

        Category existing = new Category();
        existing.setId(2);
        existing.setName("Electronics");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(current));
        when(categoryRepository.findByName(dto.getName())).thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.updateCategory(id, dto)
        );

        assertEquals("category already exists", ex.getMessage());

        verify(categoryRepository).findById(id);
        verify(categoryRepository).findByName(dto.getName());
        verify(categoryRepository, never()).save(any());
        verifyNoInteractions(categoryMapper);
    }
}