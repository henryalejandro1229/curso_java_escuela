package com.henry.escuela.repositories;

import com.henry.escuela.entities.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AulaRepository extends JpaRepository<Aula, Long> {

    boolean existsByNombreAula(String nombre);

    boolean existsByNombreAulaAndIdNot(String nombre, Long id);

}
