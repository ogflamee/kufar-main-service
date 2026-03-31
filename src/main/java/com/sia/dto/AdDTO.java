package com.sia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sia.entity.AdStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdDTO {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal price;
    private String city;

    private long viewsCount;
    private LocalDateTime createdAt;
    private AdStatus status;

    private Integer userId;
    private Integer categoryId;
}
