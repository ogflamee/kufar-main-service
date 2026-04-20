package com.sia.service.impl;

import com.sia.client.ChatClient;
import com.sia.dto.MessageDTO;
import com.sia.entity.Category;
import com.sia.entity.User;
import com.sia.exception.NotFoundException;
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

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ChatClient chatClient;

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
                    return new NotFoundException("ad not found with id " + id);
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
    public Page<AdDTO> getAdsByCategory(Integer categoryId, Pageable pageable) {
        log.info("fetching ads by categoryId={}", categoryId);
        return adRepository.findByCategory_Id(categoryId, pageable)
                .map(adMapper::toDTO);
    }

    @Override
    public List<MessageDTO> getMessagesByAdId(Integer adId) {
        try {
            return chatClient.getMessageByAdId(adId);
        } catch (Exception e) {
            log.error("error while receiving all messages for adId={}", adId);
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public void deleteAd(Integer id) {
        log.info("deleting ad with id: {}", id);

        if (!adRepository.existsById(id)) {
            log.warn("attempt to delete non-existing ad: {}", id);
            throw new NotFoundException("ad was not found.");
        }

        adRepository.deleteById(id);

        log.info("ad deleted with id: {}", id);
    }

    @Override
    @Transactional
    public AdDTO incrementViews(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ad was not found"));

        ad.setViewsCount(ad.getViewsCount() + 1);
        Ad updated = adRepository.save(ad);
        return adMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public AdDTO updateAd(Integer id, AdDTO dto) {
        log.info("updating ad with id: {}", id);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ad not found with id {}", id);
                    return new NotFoundException("ad was not found");
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

    @Override
    public List<MessageDTO> getChatForAdAndUsers(Integer adId, Integer firstUserId, Integer secondUserId) {
        try {
            return chatClient.getChatByAdAndUsers(adId, firstUserId, secondUserId);
        } catch (Exception e) {
            log.error("error while receiving messages from char-service for adId={}, firstUserId={}, secondUserId={}",
                    adId, firstUserId, secondUserId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public void sendMessage(Integer adId, Integer senderId, Integer receiverId, String text) {
        MessageDTO dto = new MessageDTO();
        dto.setAdId(adId);
        dto.setSenderId(senderId);
        dto.setReceiverId(receiverId);
        dto.setText(text);

        chatClient.sendMessage(dto);
    }
}
