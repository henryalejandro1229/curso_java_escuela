package com.henry.escuela.controller;

import com.henry.escuela.dto.calificaciones.CalificacionRequest;
import com.henry.escuela.dto.calificaciones.CalificacionResponse;
import com.henry.escuela.services.calificaciones.CalificacionesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController extends CommonController<CalificacionRequest, CalificacionResponse, CalificacionesService> {
    public CalificacionController(CalificacionesService service) {
        super(service);
    }
}
