package com.sia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sia.dto.AdDTO;

public interface AdService {

    AdDTO createAd(AdDTO dto);

    AdDTO getAdById (Integer id);

    Page<AdDTO> getAllAds(Pageable pageable);

    Page<AdDTO> searchByTitle(String keyword, Pageable pageable);

    void deleteAd(Integer id);

    AdDTO updateAd(Integer id, AdDTO dto);
}
