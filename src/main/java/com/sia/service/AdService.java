package com.sia.service;

import com.sia.dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sia.dto.AdDTO;

import java.util.List;

/**
 * Сервис для работы с объявлениями.
 * содержит бизнес-логику создания, обновления,
 * удаления и получения объявлений.
 */
public interface AdService {

    AdDTO createAd(AdDTO dto);

    AdDTO getAdById (Integer id);

    Page<AdDTO> getAllAds(Pageable pageable);

    Page<AdDTO> getAdsByCategory(Integer categoryId, Pageable pageable);

    Page<AdDTO> searchByTitle(String keyword, Pageable pageable);

    void deleteAd(Integer id);

    AdDTO updateAd(Integer id, AdDTO dto);

    List<MessageDTO> getChatForAdAndUsers(Integer adId, Integer firstUserId, Integer secondUserId);

    List<MessageDTO> getMessagesByAdId(Integer adId);

    AdDTO incrementViews(Integer id);

    void sendMessage(Integer adId, Integer senderId, Integer receiverId, String text);
}
