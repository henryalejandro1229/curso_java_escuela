package com.henry.escuela.dto.inscripciones;

import com.henry.escuela.dto.datos.DatosAlumno;
import com.henry.escuela.dto.datos.DatosGrupo;

import java.math.BigDecimal;

public record InscripcionesResponse(
        Long id,
        DatosAlumno alumno,
        DatosGrupo grupo,
        BigDecimal calificacion,
        String fechaInscripcion
) {
}
