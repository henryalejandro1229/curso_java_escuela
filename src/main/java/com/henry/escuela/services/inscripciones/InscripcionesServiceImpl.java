package com.henry.escuela.services.inscripciones;

import com.henry.escuela.dto.inscripciones.InscripcionesRequest;
import com.henry.escuela.dto.inscripciones.InscripcionesResponse;
import com.henry.escuela.entities.*;
import com.henry.escuela.enums.DiaSemana;
import com.henry.escuela.exceptions.EntidadRelacionadaException;
import com.henry.escuela.mappers.AlumnoMapper;
import com.henry.escuela.mappers.GrupoMapper;
import com.henry.escuela.mappers.InscripcionMapper;
import com.henry.escuela.repositories.AlumnoRepository;
import com.henry.escuela.repositories.CalificacionRepository;
import com.henry.escuela.repositories.GrupoRepository;
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
public class InscripcionesServiceImpl implements InscripcionesService {

    private final InscripcionRepository inscripcionRepository;

    private final InscripcionMapper inscripcionesMapper;

    private final AlumnoRepository alumnoRepository;

    private final GrupoRepository grupoRepository;

    private final CalificacionRepository calificacionRepository;

    @Override
    public List<InscripcionesResponse> listar() {
        log.info("Listando todas las inscripciones");
        return inscripcionRepository.findAll().stream()
                .map(inscripcionesMapper::entidadAResponse).toList();
    }

    @Override
    public InscripcionesResponse obtenerPorId(Long id) {
        log.info("Listando inscripción por id: " + id);

        return inscripcionesMapper.entidadAResponse(obtenerInscripcion(id));
    }

    @Override
    public InscripcionesResponse registrar(InscripcionesRequest request) {
        log.info("Registando nueva inscripción...");

        Alumno alumno = obtenerAlumno(request.idAlumno());

        Grupo grupo = obtenerGrupo(request.idGrupo());

        if (inscripcionRepository.existsByAlumnoIdAndGrupoId(request.idAlumno(), request.idGrupo())) {
            throw new IllegalArgumentException("El alumno ya fue registrado en el grupo con id: " + grupo.getId());
        }

        Inscripcion inscripcion = inscripcionesMapper.requestAEntidad(request, alumno, grupo);

        inscripcionRepository.save(inscripcion);

        log.info("Nueva inscripción registrada correctamente");

        return inscripcionesMapper.entidadAResponse(inscripcion);
    }

    @Override
    public InscripcionesResponse actualizar(InscripcionesRequest request, long id) {
        Inscripcion inscripcion = obtenerInscripcion(id);

        log.info("Actualizando inscripción con id: {}", id);

        Alumno alumno = obtenerAlumno(request.idAlumno());

        Grupo grupo = obtenerGrupo(request.idGrupo());

        if (inscripcionRepository.existsByAlumnoIdAndGrupoIdAndIdNot(request.idAlumno(), request.idGrupo(), id)) {
            throw new IllegalArgumentException("El alumno ya fue registrado en el grupo con id: " + grupo.getId());
        }

        inscripcion.actualizar(alumno, grupo);

        log.info("Inscripción actualizada correctamente");

        return inscripcionesMapper.entidadAResponse(inscripcion);
    }

    @Override
    public void eliminar(Long id) {
        Inscripcion inscripcion = obtenerInscripcion(id);

        log.info("Eliminando inscripción con id: {}", id);

        if (calificacionRepository.existsByInscripcionId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar la inscripción ya que tiene calificación asociada");

        inscripcionRepository.delete(inscripcion);

        log.info("Inscripción con id: {} eliminado correctamente", inscripcion.getId());

    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOException(inscripcionRepository, id, Inscripcion.class);
    }

    private Alumno obtenerAlumno(Long id) {
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, id, Alumno.class);
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }

    private Calificacion obtenerCalificacion(Long idInscripcion) {
        return calificacionRepository.getCalificacionByInscripcionId(idInscripcion);
    }
}
