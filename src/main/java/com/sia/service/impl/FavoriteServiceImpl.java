package com.sia.service.impl;

import com.sia.entity.Ad;
import com.sia.entity.User;
import com.sia.exception.ConflictException;
import com.sia.exception.NotFoundException;
import com.sia.repository.AdRepository;
import com.sia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.sia.dto.FavoriteDTO;
import com.sia.entity.Favorite;
import com.sia.mapper.FavoriteMapper;
import com.sia.repository.FavoriteRepository;
import com.sia.service.FavoriteService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserRepository userRepository;
    private final AdRepository adRepository;


    @Override
    @Transactional
    public FavoriteDTO addToFavorite(FavoriteDTO dto) {
        log.info("adding to favorites: userId={}, adId={}", dto.getUserId(), dto.getAdId());

        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("user couldn't be null.");
        }

        if (dto.getAdId() == null) {
            throw new IllegalArgumentException("ad couldn't be null.");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("user was not found."));

        Ad ad = adRepository.findById(dto.getAdId())
                .orElseThrow(() -> new NotFoundException("ad was not found."));

        if(ad.getUser().getId().equals(dto.getUserId())) {
            throw new IllegalArgumentException("you can't add your ad to favorites");
        }

        favoriteRepository.findByUser_IdAndAd_Id(dto.getUserId(), dto.getAdId())
                .ifPresent(favorite -> {
                    throw new ConflictException("favorite is already exists");
                });

        Favorite favorite = favoriteMapper.toEntity(dto);
        favorite.setUser(user);
        favorite.setAd(ad);
        Favorite saved = favoriteRepository.save(favorite);

        log.info("added to favorites with id: {}", saved.getId());

        return favoriteMapper.toDTO(saved);
    }

    @Override
    public Page<FavoriteDTO> getUserFavorites(Integer userId, Pageable pageable) {
        log.info("fetching favorites for user: userId={}", userId);
        return favoriteRepository.findByUser_Id(userId, pageable)
                .map(favoriteMapper::toDTO);
    }

    @Override
    @Transactional
    public void removeFromFavorites(Integer userId, Integer adId) {
        log.info("removing from favorites: userId={}, adId={}", userId, adId);

        favoriteRepository.deleteByUser_IdAndAd_Id(userId, adId);

        log.info("removed from favorites: userId={}, adId={}", userId, adId);
    }
}
