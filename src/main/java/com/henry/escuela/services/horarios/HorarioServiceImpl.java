package com.henry.escuela.services.horarios;

import com.henry.escuela.dto.horarios.HorarioRequest;
import com.henry.escuela.dto.horarios.HorarioResponse;
import com.henry.escuela.entities.Grupo;
import com.henry.escuela.entities.Horario;
import com.henry.escuela.enums.DiaSemana;
import com.henry.escuela.mappers.HorarioMapper;
import com.henry.escuela.repositories.GrupoRepository;
import com.henry.escuela.repositories.HorarioRepository;
import com.henry.escuela.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HorarioServiceImpl implements HorarioService{

    private final HorarioRepository horarioRepository;

    private final GrupoRepository grupoRepository;

    private final HorarioMapper horarioMapper;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponse> listar() {
        log.info("Listando todos los horarios");
        return horarioRepository.findAll().stream()
                .map(horarioMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioResponse obtenerPorId(Long id) {
        log.info("Listando horario por id: " + id);

        return horarioMapper.entidadAResponse(obtenerHorario(id));
    }

    @Override
    public HorarioResponse registrar(HorarioRequest request) {
        log.info("Registando nuevo horario...");

        Grupo grupo = obtenerGrupo(request.idGrupo());

        DiaSemana diaSemana = obtenerDiaSemanaPorDescripcion(request.dia());

        Horario horario = horarioMapper.requestAEntidad(request, grupo, diaSemana);

        LocalTime horaInicio = parseStringALocalTime(request.horaInicio());

        LocalTime horaFin = parseStringALocalTime(request.horaFin());

        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException(
                    "La hora de inicio debe ser menor que la hora de fin"
            );
        }

        List<Horario> horarios = horarioRepository.obtenerHorariosConflicto(diaSemana, request.idGrupo(), grupo.getAula().getId());

        validarTraslape(horarios, horaInicio, horaFin);

        horarioRepository.save(horario);

        log.info("Nuevo horario registrado correctamente");

        return horarioMapper.entidadAResponse(horario);
    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, long id) {
        Horario horario = obtenerHorario(id);

        log.info("Actualizando horario con id: {}", id);

        Grupo grupo = obtenerGrupo(request.idGrupo());

        DiaSemana diaSemana = obtenerDiaSemanaPorDescripcion(request.dia());

        LocalTime horaInicio = parseStringALocalTime(request.horaInicio());

        LocalTime horaFin = parseStringALocalTime(request.horaFin());

        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException(
                    "La hora de inicio debe ser menor que la hora de fin"
            );
        }

        List<Horario> horarios = horarioRepository.obtenerHorariosConflicto(diaSemana, request.idGrupo(), grupo.getAula().getId());

        validarTraslape(horarios, horaInicio, horaFin);

        horario.actualizar(
                grupo,
                diaSemana,
                request.horaInicio(),
                request.horaFin());

        log.info("Horario actualizado correctamente");

        return horarioMapper.entidadAResponse(horario);
    }

    @Override
    public void eliminar(Long id) {
        Horario horario = obtenerHorario(id);

        log.info("Eliminando horario con id: {}", id);

        horarioRepository.delete(horario);

        log.info("Horario con id {} eliminado correctamente", horario.getId());
    }

    private Horario obtenerHorario(Long id) {
        return ServiceUtils.obtenerEntidadOException(horarioRepository, id, Horario.class);
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }

    private DiaSemana obtenerDiaSemanaPorDescripcion(String descripcion) {
        return DiaSemana.obtenerDiaSemanaPorDescripcion(descripcion.trim());
    }

    private LocalTime parseStringALocalTime(String horaStr) {
        return LocalTime.parse(horaStr, formatter);
    }

    private void validarTraslape(List<Horario> horarios, LocalTime horaInicioNueva, LocalTime horaFinNueva) {
        horarios.stream().forEach( horario -> {
            LocalTime horaInicioExistente =
                    LocalTime.parse(horario.getHoraInicio(), formatter);

            LocalTime horaFinExistente =
                    LocalTime.parse(horario.getHoraFin(), formatter);

            boolean traslapa =
                    horaInicioExistente.isBefore(horaFinNueva)
                            && horaFinExistente.isAfter(horaInicioNueva);

            if (traslapa) {
                throw new IllegalArgumentException(
                        "El horario se traslapa con otro horario existente"
                );
            }
        });
    }
}
