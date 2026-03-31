package com.sia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sia.dto.FavoriteDTO;

public interface FavoriteService  {

    FavoriteDTO addToFavorite(FavoriteDTO dto);

    Page<FavoriteDTO> getUserFavorites(Integer userId, Pageable pageable);

    void removeFromFavorites(Integer userId, Integer adId);
}
