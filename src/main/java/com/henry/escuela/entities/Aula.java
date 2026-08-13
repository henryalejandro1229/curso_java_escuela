package com.henry.escuela.entities;

import com.henry.escuela.utils.StringCustonUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AULAS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100, unique = true)
    private String nombreAula;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    public void actualizar(String nombre, Integer capacidad) {

        StringCustonUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");

        this.nombreAula = nombre.trim();
        this.capacidad = capacidad;
    }

}
