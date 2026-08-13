package com.henry.escuela.mappers;

import com.henry.escuela.dto.datos.*;
import com.henry.escuela.dto.grupos.GrupoRequest;
import com.henry.escuela.dto.grupos.GrupoResponse;
import com.henry.escuela.entities.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GrupoMapper implements CommonMapper<GrupoRequest, GrupoResponse, Grupo> {

    private final CursoMapper cursoMapper;

    @Override
    public Grupo requestAEntidad(GrupoRequest request) {
        if (request == null) return null;

        return Grupo.builder()
                .periodo(request.periodo())
                .build();
    }

    public Grupo requestAEntidad(GrupoRequest request, Curso curso, Maestro maestro, Aula aula) {
        if (request == null) return null;

        return Grupo.builder()
                .curso(curso)
                .maestro(maestro)
                .aula(aula)
                .periodo(request.periodo())
                .build();
    }

    @Override
    public GrupoResponse entidadAResponse(Grupo entidad) {
        if (entidad == null) return null;

        DatosMaestro datosMaestro = entidadADatoMaestro(entidad.getMaestro());

        DatosCurso datosCurso = cursoMapper.entidadADatosCurso(entidad.getCurso());

        DatosAula datosAula = entidadADatoAula(entidad.getAula());

        return new GrupoResponse(
                entidad.getId(),
                datosCurso,
                datosMaestro,
                datosAula,
                horariosArrayString(entidad.getHorarios()),
                entidad.getPeriodo()
        );
    }

    private DatosMaestro entidadADatoMaestro(Maestro entidad) {
        if (entidad == null) return null;

        return new DatosMaestro(
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEmail(),
                entidad.getTelefono()
        );
    }

    private DatosAula entidadADatoAula(Aula entidad) {
        if (entidad == null) return null;

        return new DatosAula(
                entidad.getNombreAula(),
                entidad.getCapacidad()
        );
    }

    private List<String> horariosArrayString(List<Horario> horarios) {

        if (horarios == null || horarios.isEmpty())
            return List.of();

        return horarios.stream()
                .map(horario ->
                        horario.getDiaSemana() + " " + horario.getHoraInicio() + " - " + horario.getHoraFin()).toList();
    }

    public DatosGrupo entidadADatoGrupo(Grupo entidad) {
        if (entidad == null)
            return null;

        return new DatosGrupo(
                entidad.getCurso().getNombre(),
                String.join(" ",
                        entidad.getMaestro().getNombre(),
                        entidad.getMaestro().getApellidoPaterno(),
                        entidad.getMaestro().getApellidoMaterno()),
                entidad.getAula().getNombreAula(),
                entidad.getPeriodo()
        );
    }
}
