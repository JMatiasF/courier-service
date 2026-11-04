package com.logitrack_atom.courier_service.controller;

import com.logitrack_atom.courier_service.config.ConfigMarkerProperties;
import com.logitrack_atom.courier_service.dto.response.ConfiguracionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Diagnóstico administrativo de configuración, sin exponer secretos. */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminConfiguracionController {
    private final String application;
    private final Environment environment;
    private final ConfigMarkerProperties marker;

    public AdminConfiguracionController(
            @Value("${spring.application.name}") String application,
            Environment environment, ConfigMarkerProperties marker) {
        this.application = application;
        this.environment = environment;
        this.marker = marker;
    }

    /** Devuelve únicamente la identidad de la configuración cargada. */
    @GetMapping(value = "/configuracion", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ConfiguracionResponse configuracion() {
        return new ConfiguracionResponse(application, environment.getActiveProfiles(), marker.marker());
    }
}
