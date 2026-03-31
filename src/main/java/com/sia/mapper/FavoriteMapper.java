package com.sia.mapper;

import org.mapstruct.Mapper;
import com.sia.dto.FavoriteDTO;
import com.sia.entity.Favorite;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "ad.id", target = "adId")
    @Mapping(source = "ad.title", target = "adTitle")
    @Mapping(source = "ad.price", target = "price")
    FavoriteDTO toDTO(Favorite favorite);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Favorite toEntity(FavoriteDTO dto);
}
