package com.logitrack_atom.courier_service.controller;

import com.logitrack_atom.courier_service.config.ConfigMarkerProperties;
import com.logitrack_atom.courier_service.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@WebMvcTest(value = AdminConfiguracionController.class, properties = "spring.application.name=courier-service")
@AutoConfigureMockMvc
@Import({SecurityConfig.class, AdminConfiguracionControllerTest.Fixture.class})
class AdminConfiguracionControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean JwtDecoder decoder;

    @Test
    void sinAutenticacionDevuelve401() throws Exception {
        mvc.perform(get("/api/v1/admin/configuracion"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void sinRolAdminDevuelve403() throws Exception {
        mvc.perform(get("/api/v1/admin/configuracion")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_REPARTIDOR"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void conRolAdminDevuelve200YSoloDatosNoSensibles() throws Exception {
        mvc.perform(get("/api/v1/admin/configuracion")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aplicacion").value("courier-service"))
                .andExpect(jsonPath("$.marcador").value("local-test-marker"))
                .andExpect(jsonPath("$.perfilesActivos").isArray())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @TestConfiguration
    static class Fixture {
        @Bean ConfigMarkerProperties configMarkerProperties() {
            return new ConfigMarkerProperties("local-test-marker");
        }
        @Bean JwtAuthenticationConverter jwtAuthenticationConverter() {
            return new JwtAuthenticationConverter();
        }
    }
}
