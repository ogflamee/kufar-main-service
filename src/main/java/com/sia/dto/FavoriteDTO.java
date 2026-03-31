package com.sia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private Integer id;
    private Integer userId;
    private Integer adId;
    private LocalDateTime addedAt;

    private String adTitle;
    private BigDecimal price;
}
