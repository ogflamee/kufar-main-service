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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private Integer id;

    @NotNull(message = "ID отправителя обязателен.")
    @Positive(message = "ID отправителя должен быть больше 0.")
    private Integer senderId;

    @NotNull(message = "ID получателя обязателен.")
    @Positive(message = "ID получателя дожлен быть больше 0.")
    private Integer receiverId;

    @NotNull(message = "ID объявления обязателен.")
    @Positive(message = "ID объявления должен быть больше 0.")
    private Integer adId;

    @NotBlank(message = "текст сообщения обязателен.")
    @Size(min = 1, max = 2000, message = "текст должен содержать от 1 до 2000 символов.")
    private String text;

    private LocalDateTime createdAt;
}
