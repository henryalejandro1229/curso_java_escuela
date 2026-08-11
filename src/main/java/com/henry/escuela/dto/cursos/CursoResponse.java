package com.henry.escuela.dto.cursos;

public record CursoResponse(
        Long id,
        String nombre,
        String descripcion,
        Integer creditos
) {
}
