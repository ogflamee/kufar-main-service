package com.sia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для передачи данных избранных.
 * Используется для создания, обновления и отображения избранных.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {

    /**
     * уникальный идентификатор избранного
     */
    private Integer id;

    /**
     * идентификатор пользователя
     */
    @NotNull(message = "ID пользователя обязателен.")
    @Positive(message = "ID пользователя должен быть больше 0.")
    private Integer userId;

    /**
     * идентификатор объявления
     */
    @NotNull(message = "ID объявления обязателен.")
    @Positive(message = "ID объявления долен быть больше 0.")
    private Integer adId;

    /**
     * дата добавления в избранное
     */
    private LocalDateTime addedAt;

    /**
     * название объявления, добавленного в избранное
     */
    private String adTitle;

    /**
     * цена из объявления
     */
    private BigDecimal price;
}
