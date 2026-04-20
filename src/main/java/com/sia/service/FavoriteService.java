package com.sia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sia.dto.FavoriteDTO;

/**
 * Сервис для работы с избранными.
 * содержит бизнес-логику добавления,
 * удаления и получения избранных.
 */
public interface FavoriteService  {

    FavoriteDTO addToFavorite(FavoriteDTO dto);

    Page<FavoriteDTO> getUserFavorites(Integer userId, Pageable pageable);

    void removeFromFavorites(Integer userId, Integer adId);
}
