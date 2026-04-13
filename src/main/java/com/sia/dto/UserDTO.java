package com.sia.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;

    @Email(message = "некоректный email.")
    @Size(max = 255, message = "email слишком длинный.")
    private String email;

    @Size(min = 3, max = 30, message = "username должен быть от 3 до 30 символов.")
    private String username;

    @Pattern(
            regexp = "^\\+?[0-9()\\-\\s]{7,20}$",
            message = "неккоректный номер телефона."
    )
    private String phoneNumber;

    @DecimalMin(value = "0.0", inclusive = true, message = "рейтинг не может быть отрицательным")
    private Double rating;
    private LocalDateTime createdAt;
}
