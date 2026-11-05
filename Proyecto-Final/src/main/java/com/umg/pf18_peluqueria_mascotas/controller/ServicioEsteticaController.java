package com.umg.pf18_peluqueria_mascotas.controller;

import com.umg.pf18_peluqueria_mascotas.dto.ServicioEsteticaRequest;
import com.umg.pf18_peluqueria_mascotas.dto.ServicioEsteticaResponse;
import com.umg.pf18_peluqueria_mascotas.service.ServicioEsteticaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioEsteticaController {

    private final ServicioEsteticaService servicioService;

    public ServicioEsteticaController(
            ServicioEsteticaService servicioService
    ) {
        this.servicioService = servicioService;
    }

    @PostMapping
    public ResponseEntity<ServicioEsteticaResponse> crear(
            @Valid @RequestBody ServicioEsteticaRequest request
    ) {
        ServicioEsteticaResponse respuesta =
                servicioService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<ServicioEsteticaResponse>> listarTodos() {
        return ResponseEntity.ok(
                servicioService.listarTodos()
        );
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ServicioEsteticaResponse>> listarActivos() {
        return ResponseEntity.ok(
                servicioService.listarActivos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioEsteticaResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                servicioService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioEsteticaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicioEsteticaRequest request
    ) {
        return ResponseEntity.ok(
                servicioService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @PathVariable Long id
    ) {
        servicioService.desactivar(id);

        return ResponseEntity.noContent().build();
    }
}