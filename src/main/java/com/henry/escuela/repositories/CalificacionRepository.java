package com.henry.escuela.repositories;

import com.henry.escuela.entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    Calificacion getCalificacionByInscripcionId(Long isInscripcion);

    boolean existsByInscripcionId(Long idInscripcion);

    boolean existsByInscripcionIdAndIdNot(Long idInscripcion, Long idCalificacion);
}
