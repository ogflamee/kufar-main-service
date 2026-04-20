package com.sia.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sia.entity.AdStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для передачи данных объявлений.
 * Используется для создания, обновления и отображения объявлений.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdDTO {
    private Integer id;

    /**
     * уникальный идентификатор объявления
     */
    @NotBlank(message = "название объявления обязателно")
    @Size(min = 3, max = 255, message = "название должен быть от 3 до 255 символов")
    private String title;

    /**
     * название объявления
     */
    @NotBlank(message = "описание обязателен")
    @Size(min = 3, max = 2000, message = "описание должно быть от 3 до 2000 символов")
    private String description;

    /**
     * цена объявления
     */
    @NotNull(message = "цена обязательна.")
    @DecimalMin(value = "0.0", inclusive = false, message = "цена должна быть больше 0")
    private BigDecimal price;

    /**
     * город размещения объявления
     */
    @NotBlank(message = "city обязателен")
    @Size(min = 3, max = 30, message = "название города должно быть от 3 до 30 символов")
    private String city;

    /**
     * количетво просмотров на объявлении
     */
    @PositiveOrZero(message = "количество просмотров не может быть отрицательным")
    private long viewsCount;

    /**
     * дата создания объявления
     */
    private LocalDateTime createdAt;

    /**
     * статус объявления
     */
    private AdStatus status;

    /**
     * идентификатор пользователя
     */
    private Integer userId;

    /**
     * имя пользователя
     */
    private String username;

    /**
     * идентификатор категории
     */
    @NotNull(message = "ID категории обязателен")
    @Positive(message = "ID категории должен быть больше 0")
    private Integer categoryId;

    /**
     * название категории
     */
    private String categoryName;
}
