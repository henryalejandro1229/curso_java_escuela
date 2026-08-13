package com.henry.escuela.services.aulas;

import com.henry.escuela.dto.aulas.AulaRequest;
import com.henry.escuela.dto.aulas.AulaResponse;
import com.henry.escuela.entities.Aula;
import com.henry.escuela.exceptions.EntidadRelacionadaException;
import com.henry.escuela.mappers.AulaMapper;
import com.henry.escuela.repositories.AulaRepository;
import com.henry.escuela.repositories.GrupoRepository;
import com.henry.escuela.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AulaServiceImpl implements AulaService {

    private final AulaRepository aulaRepository;

    private final GrupoRepository grupoRepository;

    private final AulaMapper aulaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AulaResponse> listar() {
        log.info("Listando todas las aulas");
        return aulaRepository.findAll().stream()
                .map(aulaMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AulaResponse obtenerPorId(Long id) {
        log.info("Listando aula por id: " + id);

        return aulaMapper.entidadAResponse(obtenerAula(id));
    }

    @Override
    public AulaResponse registrar(AulaRequest request) {
        log.info("Registando nueva aula...");

        validarDatosUnicos(request);

        Aula aula = aulaMapper.requestAEntidad(request);

        aulaRepository.save(aula);

        log.info("Nueva aula {} registrada", aula.getNombreAula());

        return aulaMapper.entidadAResponse(aula);
    }

    @Override
    public AulaResponse actualizar(AulaRequest request, long id) {
        Aula aula = obtenerAula(id);

        log.info("Actualizando aula con id: {}", id);

        validarCambiosUnicos(request, id);

        aula.actualizar(
                request.nombre(),
                request.capacidad());

        log.info("Aula {} actualizada correctamente", aula.getNombreAula());

        return aulaMapper.entidadAResponse(aula);
    }

    @Override
    public void eliminar(Long id) {
        Aula aula = obtenerAula(id);

        log.info("Eliminando aula con id: {}", id);

        if (grupoRepository.existsByAulaId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el aula ya que está asignado en al menos un grupo");

        aulaRepository.delete(aula);

        log.info("Aula {} eliminado correctamente", aula.getNombreAula());

    }

    private void validarDatosUnicos(AulaRequest request) {

        log.info("Validando email único...");

        if (aulaRepository.existsByNombreAula(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe un aula registrado con el nombre: " + request.nombre());
    }

    private void validarCambiosUnicos(AulaRequest request, Long id) {

        log.info("Validando nombre único...");

        if (aulaRepository.existsByNombreAulaAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe un aula registrado con el nombre: " + request.nombre());
    }

    private Aula obtenerAula(Long id) {
        return ServiceUtils.obtenerEntidadOException(aulaRepository, id, Aula.class);
    }
}
