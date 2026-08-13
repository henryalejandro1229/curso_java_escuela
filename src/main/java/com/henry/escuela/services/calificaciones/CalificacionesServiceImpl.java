package com.henry.escuela.services.calificaciones;

import com.henry.escuela.dto.calificaciones.CalificacionRequest;
import com.henry.escuela.dto.calificaciones.CalificacionResponse;
import com.henry.escuela.entities.Calificacion;
import com.henry.escuela.entities.Curso;
import com.henry.escuela.entities.Inscripcion;
import com.henry.escuela.mappers.CalificacionMapper;
import com.henry.escuela.repositories.CalificacionRepository;
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
public class CalificacionesServiceImpl implements CalificacionesService {

    private final CalificacionRepository calificacionRepository;

    private final CalificacionMapper calificacionMapper;

    private final InscripcionRepository inscripcionRepository;

    @Override
    public List<CalificacionResponse> listar() {
        log.info("Listando todas las calificaciones");
        return calificacionRepository.findAll().stream()
                .map(calificacionMapper::entidadAResponse).toList();
    }

    @Override
    public CalificacionResponse obtenerPorId(Long id) {
        log.info("Listando calificaciones por id: " + id);

        return calificacionMapper.entidadAResponse(obtenerCalificacion(id));
    }

    @Override
    public CalificacionResponse registrar(CalificacionRequest request) {
        log.info("Registando nueva calificación...");

        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());

        Calificacion calificacion = calificacionMapper.requestAEntidad(request, inscripcion);

        if(calificacionRepository.existsByInscripcionId(request.idInscripcion())) {
            throw new IllegalArgumentException("La inscripción ya tiene una calificación registrada");
        }

        calificacionRepository.save(calificacion);

        log.info("Nueva calificación registrada");

        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public CalificacionResponse actualizar(CalificacionRequest request, long id) {
        Calificacion calificacion = obtenerCalificacion(id);

        log.info("Actualizando calificación con id: {}", id);

        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());

        if(calificacionRepository.existsByInscripcionIdAndIdNot(request.idInscripcion(), id)) {
            throw new IllegalArgumentException("La inscripción ya tiene una calificación registrada");
        }

        calificacion.actualizar(
                request.calificacion(),
                inscripcion
        );

        log.info("Calificación con id: {} actualizada correctamente", calificacion.getId());

        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public void eliminar(Long id) {
        Calificacion calificacion = obtenerCalificacion(id);

        log.info("Eliminando calificación con id: {}", id);

        calificacionRepository.delete(calificacion);

        log.info("Calificación con id {} eliminada correctamente", calificacion.getId());
    }

    private Calificacion obtenerCalificacion(Long id) {
        return ServiceUtils.obtenerEntidadOException(calificacionRepository, id, Calificacion.class);
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOException(inscripcionRepository, id, Inscripcion.class);
    }
}
