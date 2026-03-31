package com.sia.service.impl;

import com.sia.entity.Category;
import com.sia.entity.User;
import com.sia.repository.CategoryRepository;
import com.sia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.sia.dto.AdDTO;
import com.sia.entity.Ad;
import com.sia.mapper.AdMapper;
import com.sia.repository.AdRepository;
import com.sia.service.AdService;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public AdDTO createAd(AdDTO dto) {
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("user не может быыть null");
        }

        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("category не может быыть null");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("user не найден"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("category не найден"));

        Ad ad = adMapper.toEntity(dto);
        ad.setUser(user);
        ad.setCategory(category);

        Ad savedAd = adRepository.save(ad);
        return adMapper.toDto(savedAd);
    }

    @Override
    public AdDTO getAdById(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ad не найдено"));
        return adMapper.toDto(ad);
    }

    @Override
    public Page<AdDTO> getAllAds(Pageable pageable) {
        return adRepository.findAll(pageable)
                .map(adMapper::toDto);
    }

    @Override
    public Page<AdDTO> searchByTitle(String keyword, Pageable pageable) {
        return adRepository.findByTitleContainingIgnoreCase(keyword, pageable)
                .map(adMapper::toDto);
    }

    @Override
    public void deleteAd(Integer id) {
        adRepository.deleteById(id);
    }
}
