package dev.magadiflo.r2dbc.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductDTO(
        @NotBlank(message = "El name es obligatorio") String name,
        @Min(value = 1, message = "El precio debe ser mayor a cero") Float price) {
}
