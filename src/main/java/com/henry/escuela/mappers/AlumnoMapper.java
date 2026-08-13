package com.henry.escuela.mappers;

import com.henry.escuela.dto.alumnos.AlumnoRequest;
import com.henry.escuela.dto.alumnos.AlumnoResponse;
import com.henry.escuela.dto.datos.DatosAlumno;
import com.henry.escuela.dto.datos.DatosCalificacion;
import com.henry.escuela.dto.maestros.MaestroRequest;
import com.henry.escuela.entities.Alumno;
import com.henry.escuela.entities.Maestro;
import com.henry.escuela.utils.StringCustonUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AlumnoMapper implements CommonMapper<AlumnoRequest, AlumnoResponse, Alumno>{
    @Override
    public Alumno requestAEntidad(AlumnoRequest request) {
        if (request == null) return null;

        return Alumno.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .build();
    }

    public Alumno requestAEntidad(AlumnoRequest request, String email, String matricula) {

        if (request == null) return null;

        Alumno alumno = requestAEntidad(request);

        alumno.asignarDatosAcademicos(email, matricula);

        return alumno;
    }

    @Override
    public AlumnoResponse entidadAResponse(Alumno entidad) {

        if (entidad == null) return null;

        List<DatosCalificacion> calificaiones = entidadADatosCalificacion(entidad);

        return new AlumnoResponse(
                entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEmail(),
                entidad.getMatricula(),
                StringCustonUtils.localDateAString(
                        entidad.getFechaIngreso()),
                calificaiones,
                entidad.calcularPromedio()
        );
    }

    private List<DatosCalificacion> entidadADatosCalificacion(Alumno entidad) {
        if (entidad == null || entidad.getInscripciones() == null || entidad.getInscripciones().isEmpty())
            return List.of();

        return entidad.getInscripciones().stream()
                .map(inscripcion -> new DatosCalificacion(
                        inscripcion.getGrupo().getCurso().getNombre(),
                        inscripcion.getGrupo().getPeriodo(),
                        inscripcion.getCalificacion() != null ? inscripcion.getCalificacion().getCalificacion() : null
                )).toList();
    }

    public DatosAlumno entidadADatoAlumno(Alumno entidad) {
        if (entidad == null) return null;

        return new DatosAlumno(
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getMatricula(),
                entidad.getEmail(),
                entidad.getFechaIngreso().format(StringCustonUtils.FORMATOFECHA)
        );
    }
}
