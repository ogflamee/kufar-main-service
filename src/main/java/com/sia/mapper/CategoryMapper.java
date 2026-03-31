package com.sia.mapper;

import org.mapstruct.Mapper;
import com.sia.dto.CategoryDTO;
import com.sia.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO dto);
}
