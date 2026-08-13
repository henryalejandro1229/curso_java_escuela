package com.henry.escuela.dto.horarios;

import com.henry.escuela.dto.datos.DatosGrupo;

public record HorarioResponse(
        Long id,
        DatosGrupo grupo,
        String horario
) {
}
