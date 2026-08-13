package com.henry.escuela.controller;

import com.henry.escuela.dto.aulas.AulaRequest;
import com.henry.escuela.dto.aulas.AulaResponse;
import com.henry.escuela.services.aulas.AulaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aulas")
public class AulaController extends CommonController<AulaRequest, AulaResponse, AulaService>{
    public AulaController(AulaService service) {
        super(service);
    }
}
