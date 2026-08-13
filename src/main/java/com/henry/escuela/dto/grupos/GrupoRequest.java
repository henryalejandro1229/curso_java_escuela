package com.henry.escuela.dto.grupos;

import jakarta.validation.constraints.*;

public record GrupoRequest(
        @NotNull(message = "El ID del curso es requerido")
        @Positive(message = "El ID del curso debe ser positivo")
        Long idCurso,

        @NotNull(message = "El ID del maestro es requerido")
        @Positive(message = "El ID del maestro debe ser positivo")
        Long idMaestro,

        @NotNull(message = "El ID del aula es requerido")
        @Positive(message = "El ID del aula debe ser positivo")
        Long idAula,

        @NotBlank(message = "El periodo es requerido")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "El periodo debe tener el formato YYYY-MM")
        String periodo
) {
}
