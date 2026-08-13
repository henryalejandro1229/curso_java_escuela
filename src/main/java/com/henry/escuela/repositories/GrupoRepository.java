package com.henry.escuela.repositories;

import com.henry.escuela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    boolean existsByMaestroId(Long idMaestro);

    boolean existsByAulaId(Long idAula);

    boolean existsByCursoId(Long idCurso);

    boolean existsByCursoIdAndMaestroIdAndAulaIdAndPeriodo(
            Long idCurso,
            Long idMaestro,
            Long idAula,
            String periodo
    );

    boolean existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(
            Long idCurso,
            Long idMaestro,
            Long idAula,
            String periodo,
            Long idGrupo
    );


}
