package com.sia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.sia.dto.AdDTO;
import com.sia.service.AdService;
/**
 * REST-контроллер для работы с объявлениями.
 * предоставляет endpoints для создания, получения, поиска
 * обновления и удаления объявлений.
 */
@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
@Validated
public class AdController {

    private final AdService adService;

    @PostMapping
    public AdDTO createAd(@Valid @RequestBody AdDTO dto) {
        return adService.createAd(dto);
    }

    @GetMapping("/{id}")
    public AdDTO getById(@PathVariable @Positive Integer id) {
        return adService.getAdById(id);
    }

    @GetMapping
    public Page<AdDTO> getAll(Pageable pageable) {
        return adService.getAllAds(pageable);
    }

    @GetMapping("/search")
    public Page<AdDTO> search(@RequestParam @NotBlank String keyword, Pageable pageable) {
        return adService.searchByTitle(keyword, pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable @Positive Integer id) {
        adService.deleteAd(id);
    }

    @PutMapping("/{id}")
    public AdDTO updateAd(@PathVariable @Positive Integer id,
                          @Valid @RequestBody AdDTO dto) {
        return adService.updateAd(id, dto);
    }
}
