package com.sia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.sia.dto.AdDTO;
import com.sia.entity.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    AdDTO toDTO(Ad Ad);

    @Mapping( target = "user", ignore = true)
    @Mapping( target = "category", ignore = true)
    Ad toEntity(AdDTO adDTO);
}
