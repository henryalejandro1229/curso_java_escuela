package com.henry.escuela.mappers;

import com.henry.escuela.dto.datos.*;
import com.henry.escuela.dto.inscripciones.InscripcionesRequest;
import com.henry.escuela.dto.inscripciones.InscripcionesResponse;
import com.henry.escuela.entities.Alumno;
import com.henry.escuela.entities.Calificacion;
import com.henry.escuela.entities.Grupo;
import com.henry.escuela.entities.Inscripcion;
import com.henry.escuela.utils.StringCustonUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class InscripcionMapper implements CommonMapper<InscripcionesRequest, InscripcionesResponse, Inscripcion>{

    private final AlumnoMapper alumnoMapper;

    private final GrupoMapper grupoMapper;

    @Override
    public Inscripcion requestAEntidad(InscripcionesRequest request) {
        if (request == null) return null;

        return Inscripcion.builder()
                .fechaInscripcion(LocalDate.now())
                .build();
    }

    public Inscripcion requestAEntidad(InscripcionesRequest request, Alumno alumno, Grupo grupo) {
        if (request == null) return null;

        return Inscripcion.builder()
                .alumno(alumno)
                .grupo(grupo)
                .fechaInscripcion(LocalDate.now())
                .build();
    }

    @Override
    public InscripcionesResponse entidadAResponse(Inscripcion entidad) {
        if (entidad == null) return null;

        DatosAlumno datosAlumno = alumnoMapper.entidadADatoAlumno(entidad.getAlumno());

        DatosGrupo datosGrupo = grupoMapper.entidadADatoGrupo(entidad.getGrupo());

        return new InscripcionesResponse(
                entidad.getId(),
                datosAlumno,
                datosGrupo,
                entidad.getCalificacion() != null ? entidad.getCalificacion().getCalificacion() : null,
                entidad.getFechaInscripcion().format(StringCustonUtils.FORMATOFECHA)
        );
    }
}
