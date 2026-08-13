package com.henry.escuela.controller;

import com.henry.escuela.dto.alumnos.AlumnoRequest;
import com.henry.escuela.dto.alumnos.AlumnoResponse;
import com.henry.escuela.services.alumnos.AlumnoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController extends CommonController<AlumnoRequest, AlumnoResponse, AlumnoService> {
    public AlumnoController(AlumnoService service) {
        super(service);
    }
}
