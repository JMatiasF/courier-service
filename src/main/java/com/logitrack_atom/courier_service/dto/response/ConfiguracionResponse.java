package com.logitrack_atom.courier_service.dto.response;

/** Datos no sensibles de diagnóstico administrativo. */
public record ConfiguracionResponse(String aplicacion, String[] perfilesActivos, String marcador) {
    public ConfiguracionResponse {
        perfilesActivos = perfilesActivos.clone();
    }

    @Override
    public String[] perfilesActivos() {
        return perfilesActivos.clone();
    }
}
