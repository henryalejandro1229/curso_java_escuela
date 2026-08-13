package com.henry.escuela.dto.datos;

public record DatosInscripcion(
        DatosAlumno alumno,
        DatosGrupo grupo,
        String fechaInscripcion
) {
}
