package com.henry.escuela.dto.aulas;

import jakarta.validation.constraints.*;

public record AulaRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 100, message = "El nombre debe tener entre 5 y 100 caracteres")
        String nombre,

        @NotNull(message = "La capacidad es requerida")
        @Min(value = 1, message = "La capacidad mímina es 1")
        Integer capacidad
) {
}
