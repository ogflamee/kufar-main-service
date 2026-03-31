package com.sia.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.sia.dto.AdDTO;
import com.sia.service.AdService;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @PostMapping
    public AdDTO createAd(@RequestBody AdDTO dto) {
        return adService.createAd(dto);
    }

    @GetMapping("/{id}")
    public AdDTO getById(@PathVariable Integer id) {
        return adService.getAdById(id);
    }

    @GetMapping
    public Page<AdDTO> getAll(Pageable pageable) {
        return adService.getAllAds(pageable);
    }

    @GetMapping("/search")
    public Page<AdDTO> search(@RequestParam String keyword, Pageable pageable) {
        return adService.searchByTitle(keyword, pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Integer id) {
        adService.deleteAd(id);
    }
}
