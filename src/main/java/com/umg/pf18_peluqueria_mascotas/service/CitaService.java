package com.umg.pf18_peluqueria_mascotas.service;

import com.umg.pf18_peluqueria_mascotas.dto.CitaRequest;
import com.umg.pf18_peluqueria_mascotas.dto.CitaResponse;
import com.umg.pf18_peluqueria_mascotas.entity.Cita;
import com.umg.pf18_peluqueria_mascotas.entity.EstadoCita;
import com.umg.pf18_peluqueria_mascotas.entity.ServicioEstetica;
import com.umg.pf18_peluqueria_mascotas.exception.RecursoNoEncontradoException;
import com.umg.pf18_peluqueria_mascotas.exception.ReglaNegocioException;
import com.umg.pf18_peluqueria_mascotas.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final ServicioEsteticaService servicioService;

    public CitaService(
            CitaRepository citaRepository,
            ServicioEsteticaService servicioService
    ) {
        this.citaRepository = citaRepository;
        this.servicioService = servicioService;
    }

    @Transactional
    public CitaResponse crear(CitaRequest request) {
        if (request.getEstado() != null && request.getEstado() != EstadoCita.PROGRAMADA) {
            throw new ReglaNegocioException("Las citas nuevas deben iniciar PROGRAMADAS");
        }

        ServicioEstetica servicio =
                servicioService.obtenerEntidadPorId(request.getServicioId());

        if (!servicio.getActivo()) {
            throw new ReglaNegocioException(
                    "No se puede programar una cita con un servicio inactivo"
            );
        }

        EstadoCita estado = request.getEstado() == null
                ? EstadoCita.PROGRAMADA
                : request.getEstado();

        validarHorarioDisponible(
                request.getFechaHora(),
                estado,
                null
        );

        Cita cita = new Cita();

        cita.setNombreMascota(request.getNombreMascota());
        cita.setPropietario(request.getPropietario());
        cita.setEspecie(request.getEspecie());
        cita.setFechaHora(request.getFechaHora());
        cita.setEstado(estado);
        cita.setServicio(servicio);

        return convertirResponse(
                citaRepository.save(cita)
        );
    }

    public List<CitaResponse> listarTodas() {
        return citaRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    public CitaResponse buscarPorId(Long id) {
        return convertirResponse(
                obtenerEntidadPorId(id)
        );
    }

    public List<CitaResponse> listarPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado)
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    public List<CitaResponse> listarPorFecha(LocalDate fecha) {

        LocalDateTime inicio = fecha.atStartOfDay();

        LocalDateTime fin = fecha
                .plusDays(1)
                .atStartOfDay()
                .minusNanos(1);

        return citaRepository
                .findByFechaHoraBetweenOrderByFechaHoraAsc(
                        inicio,
                        fin
                )
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Transactional
    public CitaResponse actualizar(
            Long id,
            CitaRequest request
    ) {

        Cita cita = obtenerEntidadPorId(id);

        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new ReglaNegocioException("Solo se pueden editar citas PROGRAMADAS");
        }
        if (request.getEstado() != null && request.getEstado() != EstadoCita.PROGRAMADA) {
            throw new ReglaNegocioException("Use el endpoint de cambio de estado para atender o cancelar");
        }

        ServicioEstetica servicio =
                servicioService.obtenerEntidadPorId(request.getServicioId());

        if (!servicio.getActivo()) {
            throw new ReglaNegocioException(
                    "No se puede asignar un servicio inactivo"
            );
        }

        EstadoCita estado = request.getEstado() == null
                ? cita.getEstado()
                : request.getEstado();

        validarHorarioDisponible(
                request.getFechaHora(),
                estado,
                id
        );

        cita.setNombreMascota(request.getNombreMascota());
        cita.setPropietario(request.getPropietario());
        cita.setEspecie(request.getEspecie());
        cita.setFechaHora(request.getFechaHora());
        cita.setEstado(estado);
        cita.setServicio(servicio);

        return convertirResponse(
                citaRepository.save(cita)
        );
    }

    @Transactional
    public CitaResponse cambiarEstado(
            Long id,
            EstadoCita nuevoEstado
    ) {

        Cita cita = obtenerEntidadPorId(id);

        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new ReglaNegocioException("Solo una cita PROGRAMADA puede cambiar de estado");
        }
        if (nuevoEstado != EstadoCita.ATENDIDA && nuevoEstado != EstadoCita.CANCELADA) {
            throw new ReglaNegocioException("La cita solo puede pasar a ATENDIDA o CANCELADA");
        }

        cita.setEstado(nuevoEstado);

        return convertirResponse(
                citaRepository.save(cita)
        );
    }

    @Transactional
    public CitaResponse cancelar(Long id) {

        Cita cita = obtenerEntidadPorId(id);

        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new ReglaNegocioException(
                    "Solo se puede cancelar una cita PROGRAMADA"
            );
        }

        cita.setEstado(EstadoCita.CANCELADA);

        return convertirResponse(
                citaRepository.save(cita)
        );
    }

    public List<Object[]> obtenerServiciosMasSolicitados() {
        return citaRepository.obtenerServiciosMasSolicitados(
                EstadoCita.CANCELADA
        );
    }

    public BigDecimal obtenerTotalEstimadoDelDia(LocalDate fecha) {

        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();

        Object resultado = citaRepository.obtenerTotalEstimadoDelDia(
                inicio,
                fin,
                EstadoCita.CANCELADA
        );

        if (resultado instanceof BigDecimal total) {
            return total;
        }

        if (resultado instanceof Number numero) {
            return new BigDecimal(numero.toString());
        }

        return BigDecimal.ZERO;
    }

    private Cita obtenerEntidadPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró la cita con id: " + id
                        )
                );
    }

    private void validarHorarioDisponible(
            LocalDateTime fechaHora,
            EstadoCita estado,
            Long citaActualId
    ) {

        if (estado == EstadoCita.CANCELADA) {
            return;
        }

        List<Cita> citasMismoHorario =
                citaRepository.findByFechaHoraBetweenOrderByFechaHoraAsc(
                        fechaHora,
                        fechaHora
                );

        boolean existeConflicto = citasMismoHorario.stream()
                .anyMatch(cita ->
                        cita.getEstado() != EstadoCita.CANCELADA
                                && (
                                citaActualId == null
                                        || !cita.getId().equals(citaActualId)
                        )
                );

        if (existeConflicto) {
            throw new ReglaNegocioException(
                    "Ya existe una cita activa en la misma fecha y hora"
            );
        }
    }

    private CitaResponse convertirResponse(Cita cita) {

        return new CitaResponse(
                cita.getId(),
                cita.getNombreMascota(),
                cita.getPropietario(),
                cita.getEspecie(),
                cita.getFechaHora(),
                cita.getEstado(),
                cita.getServicio().getId(),
                cita.getServicio().getNombre(),
                cita.getServicio().getPrecio()
        );
    }
}