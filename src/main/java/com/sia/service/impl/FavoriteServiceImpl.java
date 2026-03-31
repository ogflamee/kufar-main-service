package com.sia.service.impl;

import com.sia.entity.Ad;
import com.sia.entity.User;
import com.sia.repository.AdRepository;
import com.sia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.sia.dto.FavoriteDTO;
import com.sia.entity.Favorite;
import com.sia.mapper.FavoriteMapper;
import com.sia.repository.FavoriteRepository;
import com.sia.service.FavoriteService;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserRepository userRepository;
    private final AdRepository adRepository;


    @Override
    public FavoriteDTO addToFavorite(FavoriteDTO dto) {
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("user не может быть null.");
        }

        if (dto.getAdId() == null) {
            throw new IllegalArgumentException("ad не может быть null.");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("user не был найден."));

        Ad ad = adRepository.findById(dto.getAdId())
                .orElseThrow(() -> new RuntimeException("ad не был найден."));

        favoriteRepository.findByUser_IdAndAd_Id(dto.getUserId(), dto.getAdId())
                .ifPresent(favorite -> {
                    throw new RuntimeException("favorite уже существует");
                });

        Favorite favorite = favoriteMapper.toEntity(dto);
        favorite.setUser(user);
        favorite.setAd(ad);

        Favorite savedFavorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDTO(savedFavorite);
    }

    @Override
    public Page<FavoriteDTO> getUserFavorites(Integer userId, Pageable pageable) {
        return favoriteRepository.findByUser_Id(userId, pageable)
                .map(favoriteMapper::toDTO);
    }

    @Override
    public void removeFromFavorites(Integer userId, Integer adId) {
        favoriteRepository.deleteByUser_IdAndAd_Id(userId, adId);
    }
}
