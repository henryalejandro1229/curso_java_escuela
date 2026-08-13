package com.henry.escuela.mappers;

import com.henry.escuela.dto.calificaciones.CalificacionRequest;
import com.henry.escuela.dto.calificaciones.CalificacionResponse;
import com.henry.escuela.dto.datos.DatosAlumno;
import com.henry.escuela.dto.datos.DatosGrupo;
import com.henry.escuela.dto.datos.DatosInscripcion;
import com.henry.escuela.entities.Calificacion;
import com.henry.escuela.entities.Inscripcion;
import com.henry.escuela.utils.StringCustonUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class CalificacionMapper implements CommonMapper<CalificacionRequest, CalificacionResponse, Calificacion>{

    private final AlumnoMapper alumnoMapper;

    private final GrupoMapper grupoMapper;

    @Override
    public Calificacion requestAEntidad(CalificacionRequest request) {
        if (request == null) return null;

        return Calificacion.builder()
                .calificacion(request.calificacion())
                .fechaRegistro(LocalDate.now())
                .build();
    }

    public Calificacion requestAEntidad(CalificacionRequest request, Inscripcion inscripcion) {
        if (request == null) return null;

        Calificacion calificacion = requestAEntidad(request);

        calificacion.asignarInscripcion(inscripcion);

        return calificacion;
    }

    @Override
    public CalificacionResponse entidadAResponse(Calificacion entidad) {
        if (entidad == null) return null;

        DatosInscripcion datosInscripcion = entidadADatoInscripcion(entidad.getInscripcion());

        return new CalificacionResponse(
                entidad.getId(),
                datosInscripcion,
                entidad.getCalificacion(),
                entidad.getFechaRegistro().format(StringCustonUtils.FORMATOFECHA)
        );
    }

    public DatosInscripcion entidadADatoInscripcion(Inscripcion entidad) {
        if (entidad == null)
            return null;

        DatosAlumno datosAlumno = alumnoMapper.entidadADatoAlumno(entidad.getAlumno());

        DatosGrupo datosGrupo = grupoMapper.entidadADatoGrupo(entidad.getGrupo());

        return new DatosInscripcion(
                datosAlumno,
                datosGrupo,
                entidad.getFechaInscripcion().format(StringCustonUtils.FORMATOFECHA)
        );
    }
}
