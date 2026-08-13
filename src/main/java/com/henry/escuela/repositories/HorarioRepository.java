package com.henry.escuela.repositories;

import com.henry.escuela.entities.Horario;
import com.henry.escuela.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    boolean existsByGrupoId(Long idGrupo);

    @Query("""
    SELECT h
    FROM Horario h
    WHERE h.diaSemana = :dia
      AND (
            h.grupo.id = :idGrupo
            OR h.grupo.aula.id = :idAula
          )
""")
    List<Horario> obtenerHorariosConflicto(
            @Param("dia") DiaSemana dia,
            @Param("idGrupo") Long idGrupo,
            @Param("idAula") Long idAula
    );

    @Query("""
    SELECT h
    FROM Horario h
    WHERE h.diaSemana = :dia
      AND (
            h.grupo.id = :idGrupo
            OR h.grupo.aula.id = :idAula
          )
      AND h.id <> :idHorario
""")
    List<Horario> obtenerHorariosConflicto(
            @Param("dia") DiaSemana dia,
            @Param("idGrupo") Long idGrupo,
            @Param("idAula") Long idAula,
            @Param("idHorario") Long idHorario
    );
}
