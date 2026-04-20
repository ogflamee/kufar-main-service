package com.sia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO для передачи данных сообщений.
 * Используется для создания, обновления и отображения сообщений.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    /**
     * уникальный идентификатор сообщения
     */
    private Integer id;

    /**
     * идентификатор отправителя
     */
    @NotNull(message = "ID отправителя обязателен.")
    @Positive(message = "ID отправителя должен быть больше 0.")
    private Integer senderId;

    /**
     * идентификатор получателя
     */
    @NotNull(message = "ID получателя обязателен.")
    @Positive(message = "ID получателя дожлен быть больше 0.")
    private Integer receiverId;

    /**
     * идентификатор объявления, где ведут чат
     */
    @NotNull(message = "ID объявления обязателен.")
    @Positive(message = "ID объявления должен быть больше 0.")
    private Integer adId;

    /**
     * текст ообщения
     */
    @NotBlank(message = "текст сообщения обязателен.")
    @Size(min = 1, max = 2000, message = "текст должен содержать от 1 до 2000 символов.")
    private String text;

    /**
     * дата создания сообщения
     */
    private LocalDateTime createdAt;
}
