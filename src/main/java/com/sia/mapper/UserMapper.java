package com.sia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.sia.dto.UserCreateDTO;
import com.sia.dto.UserDTO;
import com.sia.entity.User;

/**
 * Маппер для преобразования между сущностью пользователем и DTO.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserCreateDTO dto);
}
