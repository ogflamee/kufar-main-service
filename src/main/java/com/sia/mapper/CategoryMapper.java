package com.sia.mapper;

import org.mapstruct.Mapper;
import com.sia.dto.CategoryDTO;
import com.sia.entity.Category;

/**
 * Маппер для преобразования между сущностью категории и DTO.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO dto);
}
