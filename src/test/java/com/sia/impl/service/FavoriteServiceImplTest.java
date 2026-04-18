package com.sia.impl.service;

import com.sia.dto.FavoriteDTO;
import com.sia.entity.Ad;
import com.sia.entity.Favorite;
import com.sia.entity.User;
import com.sia.mapper.FavoriteMapper;
import com.sia.repository.AdRepository;
import com.sia.repository.FavoriteRepository;
import com.sia.repository.UserRepository;
import com.sia.service.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FavoriteMapper favoriteMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdRepository adRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    void addToFavorite_success() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(1);
        dto.setAdId(2);

        User user = new User();
        user.setId(1);

        Ad ad = new Ad();
        ad.setId(2);

        Favorite favorite = new Favorite();
        Favorite saved = new Favorite();
        saved.setId(10);

        FavoriteDTO response = new FavoriteDTO();
        response.setId(10);
        response.setUserId(1);
        response.setAdId(2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(adRepository.findById(2)).thenReturn(Optional.of(ad));
        when(favoriteRepository.findByUser_IdAndAd_Id(1, 2)).thenReturn(Optional.empty());
        when(favoriteMapper.toEntity(dto)).thenReturn(favorite);
        when(favoriteRepository.save(favorite)).thenReturn(saved);
        when(favoriteMapper.toDTO(saved)).thenReturn(response);

        FavoriteDTO result = favoriteService.addToFavorite(dto);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals(1, result.getUserId());
        assertEquals(2, result.getAdId());
        assertEquals(user, favorite.getUser());
        assertEquals(ad, favorite.getAd());

        verify(userRepository).findById(1);
        verify(adRepository).findById(2);
        verify(favoriteRepository).findByUser_IdAndAd_Id(1, 2);
        verify(favoriteMapper).toEntity(dto);
        verify(favoriteRepository).save(favorite);
        verify(favoriteMapper).toDTO(saved);
    }

    @Test
    void addToFavorite_userIdIsNull() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setAdId(2);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> favoriteService.addToFavorite(dto)
        );

        assertEquals("user couldn't be null.", ex.getMessage());

        verifyNoInteractions(userRepository, adRepository, favoriteRepository, favoriteMapper);
    }

    @Test
    void addToFavorite_adIdIsNull() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> favoriteService.addToFavorite(dto)
        );

        assertEquals("ad couldn't be null.", ex.getMessage());

        verifyNoInteractions(userRepository, adRepository, favoriteRepository, favoriteMapper);
    }

    @Test
    void addToFavorite_userNotFound() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(1);
        dto.setAdId(2);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> favoriteService.addToFavorite(dto)
        );

        assertEquals("user was not found.", ex.getMessage());
        verify(userRepository).findById(1);
        verifyNoInteractions(adRepository, favoriteRepository, favoriteMapper);
    }

    @Test
    void addToFavorite_adNotFound() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(1);
        dto.setAdId(2);

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(adRepository.findById(2)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> favoriteService.addToFavorite(dto)
        );

        assertEquals("ad was not found.", ex.getMessage());

        verify(userRepository).findById(1);
        verify(adRepository).findById(2);
        verifyNoInteractions(favoriteRepository, favoriteMapper);
    }

    @Test
    void addToFavorite_alreadyExists() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setUserId(1);
        dto.setAdId(2);

        User user = new User();
        user.setId(1);

        Ad ad = new Ad();
        ad.setId(2);

        Favorite existingFavorite = new Favorite();
        existingFavorite.setId(99);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(adRepository.findById(2)).thenReturn(Optional.of(ad));
        when(favoriteRepository.findByUser_IdAndAd_Id(1, 2)).thenReturn(Optional.of(existingFavorite));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> favoriteService.addToFavorite(dto)
        );

        assertEquals("favorite is already exists", ex.getMessage());

        verify(userRepository).findById(1);
        verify(adRepository).findById(2);
        verify(favoriteRepository).findByUser_IdAndAd_Id(1, 2);
        verifyNoMoreInteractions(favoriteRepository);
        verifyNoInteractions(favoriteMapper);
    }

    @Test
    void getUserFavorites_success() {
        Integer userId = 1;
        Pageable pageable = PageRequest.of(0, 5);

        Favorite favorite = new Favorite();
        favorite.setId(1);

        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(1);

        Page<Favorite> favoritePage = new PageImpl<>(List.of(favorite), pageable, 1);

        when(favoriteRepository.findByUser_Id(userId, pageable)).thenReturn(favoritePage);
        when(favoriteMapper.toDTO(favorite)).thenReturn(dto);

        Page<FavoriteDTO> result = favoriteService.getUserFavorites(userId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).getId());

        verify(favoriteRepository).findByUser_Id(userId, pageable);
        verify(favoriteMapper).toDTO(favorite);
    }

    @Test
    void getUserFavorites_empty() {
        Integer userId = 1;
        Pageable pageable = PageRequest.of(0, 5);

        Page<Favorite> emptyPage = Page.empty(pageable);

        when(favoriteRepository.findByUser_Id(userId, pageable)).thenReturn(emptyPage);

        Page<FavoriteDTO> result = favoriteService.getUserFavorites(userId, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());

        verify(favoriteRepository).findByUser_Id(userId, pageable);
        verifyNoInteractions(favoriteMapper);
    }

    @Test
    void removeFromFavorites_success() {
        Integer userId = 1;
        Integer adId = 2;

        favoriteService.removeFromFavorites(userId, adId);

        verify(favoriteRepository).deleteByUser_IdAndAd_Id(userId, adId);
    }
}