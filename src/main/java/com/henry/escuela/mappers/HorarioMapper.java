package com.henry.escuela.mappers;

import com.henry.escuela.dto.datos.DatosGrupo;
import com.henry.escuela.dto.horarios.HorarioRequest;
import com.henry.escuela.dto.horarios.HorarioResponse;
import com.henry.escuela.entities.Grupo;
import com.henry.escuela.entities.Horario;
import com.henry.escuela.enums.DiaSemana;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HorarioMapper implements CommonMapper<HorarioRequest, HorarioResponse, Horario>{

    private final GrupoMapper grupoMapper;

    @Override
    public Horario requestAEntidad(HorarioRequest request) {
        if (request == null) return null;

        return Horario.builder()
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .build();
    }

    public Horario requestAEntidad(HorarioRequest request, Grupo grupo, DiaSemana diaSemana) {
        if (request == null) return null;

        Horario horario = requestAEntidad(request);

        horario.asignarGrupo(grupo);

        horario.asignarDiaSemana(diaSemana);

        return horario;
    }

    @Override
    public HorarioResponse entidadAResponse(Horario entidad) {
        if (entidad == null) return null;

        DatosGrupo datoGrupo = grupoMapper.entidadADatoGrupo(entidad.getGrupo());

        return new HorarioResponse(
                entidad.getId(),
                datoGrupo,
                String.join(" ",
                        entidad.getDiaSemana().getDescripcion(),
                        entidad.getHoraInicio(),
                        entidad.getHoraFin())
        );
    }
}
