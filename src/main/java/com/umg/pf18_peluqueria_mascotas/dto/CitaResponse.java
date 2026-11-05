package com.umg.pf18_peluqueria_mascotas.dto;

import com.umg.pf18_peluqueria_mascotas.entity.EstadoCita;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CitaResponse {

    private Long id;
    private String nombreMascota;
    private String propietario;
    private String especie;
    private LocalDateTime fechaHora;
    private EstadoCita estado;

    private Long servicioId;
    private String servicioNombre;
    private BigDecimal costoEstimado;

    public CitaResponse() {
    }

    public CitaResponse(
            Long id,
            String nombreMascota,
            String propietario,
            String especie,
            LocalDateTime fechaHora,
            EstadoCita estado,
            Long servicioId,
            String servicioNombre,
            BigDecimal costoEstimado
    ) {
        this.id = id;
        this.nombreMascota = nombreMascota;
        this.propietario = propietario;
        this.especie = especie;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.servicioId = servicioId;
        this.servicioNombre = servicioNombre;
        this.costoEstimado = costoEstimado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public String getServicioNombre() {
        return servicioNombre;
    }

    public void setServicioNombre(String servicioNombre) {
        this.servicioNombre = servicioNombre;
    }

    public BigDecimal getCostoEstimado() {
        return costoEstimado;
    }

    public void setCostoEstimado(BigDecimal costoEstimado) {
        this.costoEstimado = costoEstimado;
    }
}