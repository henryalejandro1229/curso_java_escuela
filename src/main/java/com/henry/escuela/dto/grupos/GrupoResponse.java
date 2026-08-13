package com.henry.escuela.dto.grupos;

import com.henry.escuela.dto.datos.DatosAula;
import com.henry.escuela.dto.datos.DatosCurso;
import com.henry.escuela.dto.datos.DatosMaestro;
import com.henry.escuela.entities.Aula;
import com.henry.escuela.entities.Curso;
import com.henry.escuela.entities.Horario;
import com.henry.escuela.entities.Maestro;

import java.util.List;

public record GrupoResponse(
        Long id,
        DatosCurso curso,
        DatosMaestro maestro,
        DatosAula aula,
        List<String> horarios,
        String periodo
) {
}
