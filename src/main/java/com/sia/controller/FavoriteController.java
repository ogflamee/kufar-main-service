package com.sia.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.sia.dto.FavoriteDTO;
import com.sia.service.FavoriteService;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public FavoriteDTO add(@RequestBody FavoriteDTO dto) {
        return favoriteService.addToFavorite(dto);
    }

    @GetMapping("/user/{userId}")
    public Page<FavoriteDTO> getFavorites(@PathVariable Integer userId, Pageable pageable) {
        return favoriteService.getUserFavorites(userId, pageable);
    }

    @DeleteMapping
    public void remove(@RequestParam Integer userId,@RequestParam Integer adId) {
        favoriteService.removeFromFavorites(userId, adId);
    }
}
