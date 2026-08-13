package com.henry.escuela.mappers;

import com.henry.escuela.dto.aulas.AulaRequest;
import com.henry.escuela.dto.aulas.AulaResponse;
import com.henry.escuela.entities.Aula;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper implements CommonMapper<AulaRequest, AulaResponse, Aula> {

    @Override
    public Aula requestAEntidad(AulaRequest request) {
        if (request == null) return null;

        return Aula.builder()
                .nombreAula(request.nombre())
                .capacidad(request.capacidad())
                .build();
    }

    @Override
    public AulaResponse entidadAResponse(Aula entidad) {
        if (entidad == null) return null;

        return new AulaResponse(
                entidad.getId(),
                entidad.getNombreAula(),
                entidad.getCapacidad()
        );
    }
}
