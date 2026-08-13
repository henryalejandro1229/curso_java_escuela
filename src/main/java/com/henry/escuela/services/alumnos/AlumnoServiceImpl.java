package com.henry.escuela.services.alumnos;

import com.henry.escuela.dto.alumnos.AlumnoRequest;
import com.henry.escuela.dto.alumnos.AlumnoResponse;
import com.henry.escuela.entities.Alumno;
import com.henry.escuela.entities.Maestro;
import com.henry.escuela.exceptions.EntidadRelacionadaException;
import com.henry.escuela.mappers.AlumnoMapper;
import com.henry.escuela.repositories.AlumnoRepository;
import com.henry.escuela.repositories.InscripcionRepository;
import com.henry.escuela.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;

    private final InscripcionRepository inscripcionRepository;

    private final AlumnoMapper alumnoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> listar() {
        log.info("Listando todos los alumnos");
        return alumnoRepository.findAll().stream()
                .map(alumnoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse obtenerPorId(Long id) {
        log.info("Listando alumno por id: " + id);

        return alumnoMapper.entidadAResponse(obtenerAlumno(id));
    }

    @Override
    public AlumnoResponse registrar(AlumnoRequest request) {
        log.info("Registando nuevo alumno...");

        String matriculaGenerada = generarMatricula(request);

        String emailGenerado = generarEmail(request);

        Alumno alumno = alumnoMapper.requestAEntidad(
                request,
                emailGenerado,
                matriculaGenerada);

        alumnoRepository.save(alumno);

        log.info("Nuevo alumno {} registrado correctamente", alumno.getNombre());

        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, long id) {

        Alumno alumno = obtenerAlumno(id);

        log.info("Actualizando alumno con id {}", id);

        if (alumno.cambioEnDatos(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        )) {
            alumno.actualizar(
                    request.nombre().trim(),
                    request.apellidoPaterno().trim(),
                    request.apellidoMaterno().trim(),
                    generarEmail(request),
                    generarMatricula(request)
            );

            log.info("Datos acadpemicos regenerados para el alumno con id {}", id);
        };
        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public void eliminar(Long id) {
        Alumno alumno = obtenerAlumno(id);

        log.info("Eliminando alumno con id: {}", id);

        if (inscripcionRepository.existsByAlumnoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el alumno ya que tiene inscripciones asignadas");

        alumnoRepository.delete(alumno);

        log.info("Alumno {} eliminado correctamente", alumno.getNombre());

    }

    private Alumno obtenerAlumno(Long id) {
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, id, Alumno.class);
    }

    private String generarMatricula(AlumnoRequest request) {
        log.info("Generando matrícula ...");

        return alumnoRepository.generarMatricula(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );
    }

    private String generarEmail(AlumnoRequest request) {
        log.info("Generando email ...");

        return alumnoRepository.generarEmail(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );
    }
}
