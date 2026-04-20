package com.sia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных пользователей.
 * Используется для создания пользователей.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    /**
     * электронная почта пользоватля
     */
    @Email(message = "некоректный email.")
    @NotBlank(message = "email обязателен.")
    private String email;

    /**
     * имя пользователя
     */
    @NotBlank(message = "username обязателен.")
    @Size(min = 3, max = 30, message = "username должен быть от 3 до 30 символов.")
    private String username;

    /**
     * пароль пользователя
     */
    @NotBlank(message = "password обязателен.")
    @Size(min = 6, max = 30, message = "password должен быть от 6 до 30 символов.")
    private String password;

    /**
     * номер телефона пользователя
     */
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "некорректный phoneNumber")
    private String phoneNumber;
}
