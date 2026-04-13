package com.sia.service.impl;

import com.sia.entity.Category;
import com.sia.entity.User;
import com.sia.repository.CategoryRepository;
import com.sia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.sia.dto.AdDTO;
import com.sia.entity.Ad;
import com.sia.mapper.AdMapper;
import com.sia.repository.AdRepository;
import com.sia.service.AdService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AdDTO createAd(AdDTO dto) {
        log.info("creating ad with title: {}", dto.getTitle());

        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("user couldn't be null");
        }

        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("category couldn't be null");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("user was not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("category was not found"));

        Ad ad = adMapper.toEntity(dto);
        ad.setUser(user);
        ad.setCategory(category);

        Ad savedAd = adRepository.save(ad);
        log.info("ad created with id: {}", savedAd.getId());
        return adMapper.toDTO(savedAd);
    }

    @Override
    public AdDTO getAdById(Integer id) {
        log.info("fetching ad with id: {}", id);
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ad not found with id: {}", id);
                return new RuntimeException("ad not found with id " + id);
                });
        return adMapper.toDTO(ad);
    }

    @Override
    public Page<AdDTO> getAllAds(Pageable pageable) {
        log.info("fetching all ads with pagination: {}", pageable);
        return adRepository.findAll(pageable)
                .map(adMapper::toDTO);
    }

    @Override
    public Page<AdDTO> searchByTitle(String keyword, Pageable pageable) {
        log.info("searching ads with pagination: {}", pageable);
        return adRepository.findByTitleContainingIgnoreCase(keyword, pageable)
                .map(adMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteAd(Integer id) {
        log.info("deleting ad with id: {}", id);

        if(!adRepository.existsById(id)) {
            log.warn("attempt to delete non-existing ad: {}", id);
            throw new RuntimeException("ad was not found.");
        }

        adRepository.deleteById(id);

        log.info("ad deleted with id: {}", id);
    }

    @Override
    @Transactional
    public AdDTO updateAd(Integer id, AdDTO dto) {
        log.info("updating ad with id: {}", id);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ad not found with id {}", id);
                    return new RuntimeException("ad was not found");
                });

        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setCity(dto.getCity());
        ad.setStatus(dto.getStatus());

        Ad updated = adRepository.save(ad);

        log.info("ad updated with id: {}", id);

        return adMapper.toDTO(updated);
    }
}
