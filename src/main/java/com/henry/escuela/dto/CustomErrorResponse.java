package com.henry.escuela.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}
