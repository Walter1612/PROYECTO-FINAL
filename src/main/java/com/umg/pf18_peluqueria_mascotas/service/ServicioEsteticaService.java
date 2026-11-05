package com.umg.pf18_peluqueria_mascotas.service;

import com.umg.pf18_peluqueria_mascotas.dto.ServicioEsteticaRequest;
import com.umg.pf18_peluqueria_mascotas.dto.ServicioEsteticaResponse;
import com.umg.pf18_peluqueria_mascotas.entity.ServicioEstetica;
import com.umg.pf18_peluqueria_mascotas.exception.RecursoDuplicadoException;
import com.umg.pf18_peluqueria_mascotas.exception.RecursoNoEncontradoException;
import com.umg.pf18_peluqueria_mascotas.repository.ServicioEsteticaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioEsteticaService {

    private final ServicioEsteticaRepository servicioRepository;

    public ServicioEsteticaService(ServicioEsteticaRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public ServicioEsteticaResponse crear(ServicioEsteticaRequest request) {

        if (servicioRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RecursoDuplicadoException(
                    "Ya existe un servicio con el nombre: " + request.getNombre()
            );
        }

        ServicioEstetica servicio = new ServicioEstetica();

        servicio.setNombre(request.getNombre());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setPrecio(request.getPrecio());
        servicio.setDuracionMinutos(request.getDuracionMinutos());
        servicio.setActivo(
                request.getActivo() == null ? true : request.getActivo()
        );

        ServicioEstetica guardado = servicioRepository.save(servicio);

        return convertirResponse(guardado);
    }

    public List<ServicioEsteticaResponse> listarTodos() {
        return servicioRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    public List<ServicioEsteticaResponse> listarActivos() {
        return servicioRepository.findByActivoTrue()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    public ServicioEsteticaResponse buscarPorId(Long id) {

        ServicioEstetica servicio = servicioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el servicio con id: " + id
                        )
                );

        return convertirResponse(servicio);
    }

    public ServicioEstetica obtenerEntidadPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el servicio con id: " + id
                        )
                );
    }

    public ServicioEsteticaResponse actualizar(
            Long id,
            ServicioEsteticaRequest request
    ) {

        ServicioEstetica servicio = servicioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el servicio con id: " + id
                        )
                );

        servicioRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(existente -> {
                    if (!existente.getId().equals(id)) {
                        throw new RecursoDuplicadoException(
                                "Ya existe un servicio con el nombre: "
                                        + request.getNombre()
                        );
                    }
                });

        servicio.setNombre(request.getNombre());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setPrecio(request.getPrecio());
        servicio.setDuracionMinutos(request.getDuracionMinutos());

        if (request.getActivo() != null) {
            servicio.setActivo(request.getActivo());
        }

        ServicioEstetica actualizado = servicioRepository.save(servicio);

        return convertirResponse(actualizado);
    }

    public void desactivar(Long id) {

        ServicioEstetica servicio = servicioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el servicio con id: " + id
                        )
                );

        servicio.setActivo(false);

        servicioRepository.save(servicio);
    }

    private ServicioEsteticaResponse convertirResponse(
            ServicioEstetica servicio
    ) {

        return new ServicioEsteticaResponse(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecio(),
                servicio.getDuracionMinutos(),
                servicio.getActivo()
        );
    }
}