package com.henry.escuela.services.cursos;

import com.henry.escuela.dto.cursos.CursoRequest;
import com.henry.escuela.dto.cursos.CursoResponse;
import com.henry.escuela.entities.Curso;
import com.henry.escuela.exceptions.EntidadRelacionadaException;
import com.henry.escuela.mappers.CursoMapper;
import com.henry.escuela.repositories.CursoRepository;
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
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    private final GrupoRepository grupoRepository;

    private final CursoMapper cursoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {
        log.info("Listando todos los cursos");
        return cursoRepository.findAll().stream()
                .map(cursoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerPorId(Long id) {
        log.info("Listando cursos por id: " + id);

        return cursoMapper.entidadAResponse(obtenerCurso(id));
    }

    @Override
    public CursoResponse registrar(CursoRequest request) {
        log.info("Registando nuevo curso...");

        validarDatosUnicos(request);

        Curso curso = cursoMapper.requestAEntidad(request);

        cursoRepository.save(curso);

        log.info("Nuevo curso {} registrado", curso.getNombre());

        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public CursoResponse actualizar(CursoRequest request, long id) {
        Curso curso = obtenerCurso(id);

        log.info("Actualizando curso con id: {}", id);

        validarCambiosUnicos(request, id);

        curso.actualizar(
                request.nombre(),
                request.descripcion(),
                request.creditos());

        log.info("Curso {} actualizado correctamente", curso.getNombre());

        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public void eliminar(Long id) {
        Curso curso = obtenerCurso(id);

        log.info("Eliminando curso con id: {}", id);

        if (grupoRepository.existsByCursoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el curso ya que está asignado en al menos un grupo");

        cursoRepository.delete(curso);

        log.info("Maestro {} eliminado correctamente", curso.getNombre());
    }

    private void validarDatosUnicos(CursoRequest request) {

        log.info("Validando nombre único...");

        if (cursoRepository.existsByNombre(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe un curso registrado con el nombre: " + request.nombre());
    }

    private void validarCambiosUnicos(CursoRequest request, Long id) {

        log.info("Validando nombre único...");

        if (cursoRepository.existsByNombreAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe un curso registrado con el nombre: " + request.nombre());
    }

    private Curso obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOException(cursoRepository, id, Curso.class);
    }
}
