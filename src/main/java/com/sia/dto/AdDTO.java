package com.sia.dto;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "название объявления обязателно")
    @Size(min = 3, max = 255, message = "название должен быть от 3 до 255 символов")
    private String title;

    @NotBlank(message = "описание обязателен")
    @Size(min = 3, max = 2000, message = "описание должно быть от 3 до 2000 символов")
    private String description;

    @NotNull(message = "цена обязательна.")
    @DecimalMin(value = "0.0", inclusive = false, message = "цена должна быть больше 0")
    private BigDecimal price;

    @NotBlank(message = "city обязателен")
    @Size(min = 3, max = 30, message = "название города должно быть от 3 до 30 символов")
    private String city;

    @PositiveOrZero(message = "количество просмотров не может быть отрицательным")
    private long viewsCount;

    private LocalDateTime createdAt;
    private AdStatus status;

    private Integer userId;
    private String username;

    @NotNull(message = "ID категории обязателен")
    @Positive(message = "ID категории должен быть больше 0")
    private Integer categoryId;
    private String categoryName;
}
