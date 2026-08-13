package com.henry.escuela.repositories;

import com.henry.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    boolean existsByAlumnoId(Long idAlumno);

    boolean existsByAlumnoIdAndGrupoId(Long idAlumno, Long idGrupo);

    boolean existsByAlumnoIdAndGrupoIdAndIdNot(Long idAlumno, Long idGrupo, Long idInscripcion);

    boolean existsByGrupoId(Long idGrupo);
}
