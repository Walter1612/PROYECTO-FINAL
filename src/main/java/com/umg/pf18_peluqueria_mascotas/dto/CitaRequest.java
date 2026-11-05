package com.umg.pf18_peluqueria_mascotas.dto;

import com.umg.pf18_peluqueria_mascotas.entity.EstadoCita;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CitaRequest {

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 80, message = "El nombre de la mascota no puede superar 80 caracteres")
    private String nombreMascota;

    @NotBlank(message = "El nombre del propietario es obligatorio")
    @Size(max = 100, message = "El propietario no puede superar 100 caracteres")
    private String propietario;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 50, message = "La especie no puede superar 50 caracteres")
    private String especie;

    @NotNull(message = "La fecha y hora son obligatorias")
    @FutureOrPresent(message = "La fecha y hora no pueden estar en el pasado")
    private LocalDateTime fechaHora;

    // Opcional: si se omite, una cita nueva comienza PROGRAMADA.
    private EstadoCita estado;

    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;

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
}