package com.sia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "ID пользователя обязателен.")
    @Positive(message = "ID пользователя должен быть больше 0.")
    private Integer userId;

    @NotNull(message = "ID объявления обязателен.")
    @Positive(message = "ID объявления долен быть больше 0.")
    private Integer adId;

    private LocalDateTime addedAt;
    private String adTitle;
    private BigDecimal price;
}
