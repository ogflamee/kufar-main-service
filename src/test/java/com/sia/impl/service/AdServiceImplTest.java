package com.sia.impl.service;

import com.sia.dto.AdDTO;
import com.sia.entity.Ad;
import com.sia.entity.AdStatus;
import com.sia.entity.Category;
import com.sia.entity.User;
import com.sia.mapper.AdMapper;
import com.sia.repository.AdRepository;
import com.sia.repository.CategoryRepository;
import com.sia.repository.UserRepository;
import com.sia.service.impl.AdServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private AdMapper adMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdServiceImpl adService;

    @Test
    void createAd_success() {
        AdDTO dto = new AdDTO();
        dto.setTitle("iPhone 13");
        dto.setDescription("Good phone");
        dto.setPrice(BigDecimal.valueOf(1500));
        dto.setCity("Minsk");
        dto.setStatus(AdStatus.ACTIVE);
        dto.setUserId(1);
        dto.setCategoryId(2);

        User user = new User();
        user.setId(1);

        Category category = new Category();
        category.setId(2);

        Ad ad = new Ad();
        Ad savedAd = new Ad();
        savedAd.setId(10);

        AdDTO response = new AdDTO();
        response.setId(10);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(category));
        when(adMapper.toEntity(dto)).thenReturn(ad);
        when(adRepository.save(ad)).thenReturn(savedAd);
        when(adMapper.toDTO(savedAd)).thenReturn(response);

        AdDTO result = adService.createAd(dto);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals(user, ad.getUser());
        assertEquals(category, ad.getCategory());

        verify(userRepository).findById(1);
        verify(categoryRepository).findById(2);
        verify(adMapper).toEntity(dto);
        verify(adRepository).save(ad);
        verify(adMapper).toDTO(savedAd);
    }

    @Test
    void createAd_userIdIsNull() {
        AdDTO dto = new AdDTO();
        dto.setCategoryId(1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> adService.createAd(dto)
        );

        assertEquals("user couldn't be null", ex.getMessage());

        verifyNoInteractions(userRepository, categoryRepository, adRepository, adMapper);
    }

    @Test
    void createAd_categoryIdIsNull() {
        AdDTO dto = new AdDTO();
        dto.setUserId(1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> adService.createAd(dto)
        );

        assertEquals("category couldn't be null", ex.getMessage());

        verifyNoInteractions(userRepository, categoryRepository, adRepository, adMapper);
    }

    @Test
    void createAd_userNotFound() {
        AdDTO dto = new AdDTO();
        dto.setUserId(1);
        dto.setCategoryId(2);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adService.createAd(dto)
        );

        assertEquals("user was not found", ex.getMessage());

        verify(userRepository).findById(1);
        verifyNoInteractions(categoryRepository, adRepository, adMapper);
    }

    @Test
    void createAd_categoryNotFound() {
        AdDTO dto = new AdDTO();
        dto.setUserId(1);
        dto.setCategoryId(2);

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adService.createAd(dto)
        );

        assertEquals("category was not found", ex.getMessage());

        verify(userRepository).findById(1);
        verify(categoryRepository).findById(2);
        verifyNoInteractions(adRepository, adMapper);
    }

    @Test
    void getAdById_success() {
        Integer id = 1;

        Ad ad = new Ad();
        ad.setId(id);

        AdDTO dto = new AdDTO();
        dto.setId(id);

        when(adRepository.findById(id)).thenReturn(Optional.of(ad));
        when(adMapper.toDTO(ad)).thenReturn(dto);

        AdDTO result = adService.getAdById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());

        verify(adRepository).findById(id);
        verify(adMapper).toDTO(ad);
    }

    @Test
    void getAdById_notFound() {
        Integer id = 1;

        when(adRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adService.getAdById(id)
        );

        assertEquals("ad not found with id 1", ex.getMessage());

        verify(adRepository).findById(id);
        verifyNoInteractions(adMapper);
    }

    @Test
    void getAllAds_success() {
        Pageable pageable = PageRequest.of(0, 5);

        Ad ad = new Ad();
        ad.setId(1);

        AdDTO dto = new AdDTO();
        dto.setId(1);

        Page<Ad> adPage = new PageImpl<>(List.of(ad), pageable, 1);

        when(adRepository.findAll(pageable)).thenReturn(adPage);
        when(adMapper.toDTO(ad)).thenReturn(dto);

        Page<AdDTO> result = adService.getAllAds(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).getId());

        verify(adRepository).findAll(pageable);
        verify(adMapper).toDTO(ad);
    }

    @Test
    void searchByTitle_success() {
        Pageable pageable = PageRequest.of(0, 5);
        String keyword = "iphone";

        Ad ad = new Ad();
        ad.setId(1);

        AdDTO dto = new AdDTO();
        dto.setId(1);

        Page<Ad> adPage = new PageImpl<>(List.of(ad), pageable, 1);

        when(adRepository.findByTitleContainingIgnoreCase(keyword, pageable)).thenReturn(adPage);
        when(adMapper.toDTO(ad)).thenReturn(dto);

        Page<AdDTO> result = adService.searchByTitle(keyword, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).getId());

        verify(adRepository).findByTitleContainingIgnoreCase(keyword, pageable);
        verify(adMapper).toDTO(ad);
    }

    @Test
    void deleteAd_success() {
        Integer id = 1;

        when(adRepository.existsById(id)).thenReturn(true);

        adService.deleteAd(id);

        verify(adRepository).existsById(id);
        verify(adRepository).deleteById(id);
    }

    @Test
    void deleteAd_notFound() {
        Integer id = 1;

        when(adRepository.existsById(id)).thenReturn(false);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adService.deleteAd(id)
        );

        assertEquals("ad was not found.", ex.getMessage());

        verify(adRepository).existsById(id);
        verify(adRepository, never()).deleteById(anyInt());
    }

    @Test
    void updateAd_success() {
        Integer id = 1;

        AdDTO dto = new AdDTO();
        dto.setTitle("Updated title");
        dto.setDescription("Updated description");
        dto.setPrice(BigDecimal.valueOf(2000));
        dto.setCity("Grodno");
        dto.setStatus(AdStatus.RESERVED);

        Ad ad = new Ad();
        ad.setId(id);

        Ad updated = new Ad();
        updated.setId(id);

        AdDTO response = new AdDTO();
        response.setId(id);
        response.setTitle("Updated title");

        when(adRepository.findById(id)).thenReturn(Optional.of(ad));
        when(adRepository.save(ad)).thenReturn(updated);
        when(adMapper.toDTO(updated)).thenReturn(response);

        AdDTO result = adService.updateAd(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated title", ad.getTitle());
        assertEquals("Updated description", ad.getDescription());
        assertEquals(BigDecimal.valueOf(2000), ad.getPrice());
        assertEquals("Grodno", ad.getCity());
        assertEquals(AdStatus.RESERVED, ad.getStatus());

        verify(adRepository).findById(id);
        verify(adRepository).save(ad);
        verify(adMapper).toDTO(updated);
    }

    @Test
    void updateAd_notFound() {
        Integer id = 1;
        AdDTO dto = new AdDTO();

        when(adRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adService.updateAd(id, dto)
        );

        assertEquals("ad was not found", ex.getMessage());

        verify(adRepository).findById(id);
        verify(adRepository, never()).save(any());
        verifyNoInteractions(adMapper);
    }
}