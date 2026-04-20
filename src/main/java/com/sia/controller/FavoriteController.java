package com.sia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.sia.dto.FavoriteDTO;
import com.sia.service.FavoriteService;

/**
 * REST-контроллер для работы с избранными.
 * предоставляет endpoints для сдобавления, просмотра
 * и удаления избранных.
 */
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public FavoriteDTO add(@Valid @RequestBody FavoriteDTO dto) {
        return favoriteService.addToFavorite(dto);
    }

    @GetMapping("/user/{userId}")
    public Page<FavoriteDTO> getFavorites(@PathVariable @Positive Integer userId, Pageable pageable) {
        return favoriteService.getUserFavorites(userId, pageable);
    }

    @DeleteMapping
    public void remove(@RequestParam @Positive Integer userId,
                       @RequestParam @Positive Integer adId) {
        favoriteService.removeFromFavorites(userId, adId);
    }
}
