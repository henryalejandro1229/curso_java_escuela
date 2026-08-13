package com.henry.escuela.entities;

import com.henry.escuela.utils.StringCustonUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CURSOS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @Column(name = "CREDITOS", nullable = false)
    private Integer creditos;

    public void actualizar(String nombre, String descripcion, Integer creditos) {

        StringCustonUtils.validarTamanio(nombre, 1, 100,
                "El nombre es requerido y debe tener entre 1 y 100 caracteres");

        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();
        this.creditos = creditos;
    }
}
