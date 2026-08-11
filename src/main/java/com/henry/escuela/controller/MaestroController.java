package com.henry.escuela.controller;

import com.henry.escuela.dto.maestros.MaestroRequest;
import com.henry.escuela.dto.maestros.MaestroResponse;
import com.henry.escuela.services.maestros.MaestroService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maestros")
public class MaestroController extends CommonController<MaestroRequest, MaestroResponse, MaestroService> {

    public MaestroController(MaestroService service) {
        super(service);
    }
}
