package com.umg.pf18_peluqueria_mascotas.controller;

import com.umg.pf18_peluqueria_mascotas.dto.CitaRequest;
import com.umg.pf18_peluqueria_mascotas.dto.CitaResponse;
import com.umg.pf18_peluqueria_mascotas.entity.EstadoCita;
import com.umg.pf18_peluqueria_mascotas.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<CitaResponse> crear(
            @Valid @RequestBody CitaRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(citaService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<CitaResponse>> listarTodas() {
        return ResponseEntity.ok(
                citaService.listarTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                citaService.buscarPorId(id)
        );
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CitaResponse>> listarPorEstado(
            @PathVariable EstadoCita estado
    ) {
        return ResponseEntity.ok(
                citaService.listarPorEstado(estado)
        );
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<CitaResponse>> listarPorFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha
    ) {
        return ResponseEntity.ok(
                citaService.listarPorFecha(fecha)
        );
    }

    @GetMapping("/indicadores/servicios-mas-solicitados")
    public ResponseEntity<List<Object[]>> obtenerServiciosMasSolicitados() {
        return ResponseEntity.ok(
                citaService.obtenerServiciosMasSolicitados()
        );
    }

    @GetMapping("/indicadores/total-estimado/{fecha}")
    public ResponseEntity<BigDecimal> obtenerTotalEstimadoDelDia(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha
    ) {
        return ResponseEntity.ok(
                citaService.obtenerTotalEstimadoDelDia(fecha)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaRequest request
    ) {
        return ResponseEntity.ok(
                citaService.actualizar(id, request)
        );
    }

    @PatchMapping("/{id}/estado/{estado}")
    public ResponseEntity<CitaResponse> cambiarEstado(
            @PathVariable Long id,
            @PathVariable EstadoCita estado
    ) {
        return ResponseEntity.ok(
                citaService.cambiarEstado(id, estado)
        );
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<CitaResponse> cancelar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                citaService.cancelar(id)
        );
    }
}