package com.henry.escuela.entities;

import com.henry.escuela.dto.datos.DatosMaestro;
import com.henry.escuela.utils.StringCustonUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GRUPOS", uniqueConstraints = @UniqueConstraint(
        name = "GRUPO_CU_MA_AU_PE_UK",
        columnNames = {"ID_CURSO", "ID_MAESTRO", "ID_AULA", "PERIODO"}
))
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", length = 20, nullable = false)
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "El periodo debe tener el formato YYYY-MM")
    private String periodo;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "grupo",
            orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Horario> horarios = new ArrayList<>();

    public void actualizar(Curso curso, Maestro maestro, Aula aula, String periodo) {
        this.curso = curso;
        this.maestro = maestro;
        this.aula = aula;
        this.periodo = periodo;
    }
}
