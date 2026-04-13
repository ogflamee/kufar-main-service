package com.sia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Integer id;

    @NotBlank(message = "название категории обязательно.")
    @Size(min = 3, max = 50, message = "название категории должно быть от 2 до 50 символов.")
    private String name;
}
