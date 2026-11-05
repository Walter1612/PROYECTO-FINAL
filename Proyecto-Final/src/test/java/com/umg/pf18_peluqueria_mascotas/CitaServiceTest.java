package com.umg.pf18_peluqueria_mascotas;

import com.umg.pf18_peluqueria_mascotas.dto.CitaRequest;
import com.umg.pf18_peluqueria_mascotas.entity.*;
import com.umg.pf18_peluqueria_mascotas.exception.ReglaNegocioException;
import com.umg.pf18_peluqueria_mascotas.repository.CitaRepository;
import com.umg.pf18_peluqueria_mascotas.service.CitaService;
import com.umg.pf18_peluqueria_mascotas.service.ServicioEsteticaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CitaServiceTest {
    @Mock CitaRepository repo;
    @Mock ServicioEsteticaService servicios;
    CitaService sut;
    private final LocalDateTime fecha = LocalDateTime.now().plusDays(10).withNano(0);

    @BeforeEach void preparar() {
        MockitoAnnotations.openMocks(this);
        sut = new CitaService(repo, servicios);
    }
    private ServicioEstetica servicio() {
        ServicioEstetica s = new ServicioEstetica();
        s.setId(1L); s.setNombre("Baño"); s.setActivo(true); s.setPrecio(new BigDecimal("75.00"));
        return s;
    }
    private Cita cita(EstadoCita estado) {
        Cita c = new Cita(); c.setId(5L); c.setEstado(estado); c.setFechaHora(fecha);
        c.setNombreMascota("Luna"); c.setPropietario("Demo"); c.setEspecie("Perro"); c.setServicio(servicio());
        return c;
    }
    private CitaRequest request() {
        CitaRequest r = new CitaRequest(); r.setServicioId(1L); r.setFechaHora(fecha);
        r.setNombreMascota("Luna"); r.setPropietario("Demo"); r.setEspecie("Perro");
        return r;
    }
    @Test void creacionDebeIniciarProgramada() {
        when(servicios.obtenerEntidadPorId(1L)).thenReturn(servicio());
        when(repo.findByFechaHoraBetweenOrderByFechaHoraAsc(fecha, fecha)).thenReturn(List.of());
        when(repo.save(any(Cita.class))).thenAnswer(inv -> { Cita c = inv.getArgument(0); c.setId(5L); return c; });
        assertEquals(EstadoCita.PROGRAMADA, sut.crear(request()).getEstado());
    }
    @Test void creacionNoAceptaEstadoAtendida() {
        CitaRequest r = request(); r.setEstado(EstadoCita.ATENDIDA);
        assertThrows(ReglaNegocioException.class, () -> sut.crear(r));
        verify(repo, never()).save(any());
    }
    @Test void horarioOcupadoSeRechaza() {
        when(servicios.obtenerEntidadPorId(1L)).thenReturn(servicio());
        when(repo.findByFechaHoraBetweenOrderByFechaHoraAsc(fecha, fecha)).thenReturn(List.of(cita(EstadoCita.PROGRAMADA)));
        assertThrows(ReglaNegocioException.class, () -> sut.crear(request()));
        verify(repo, never()).save(any());
    }
    @Test void horarioCanceladoSePuedeReutilizar() {
        when(servicios.obtenerEntidadPorId(1L)).thenReturn(servicio());
        when(repo.findByFechaHoraBetweenOrderByFechaHoraAsc(fecha, fecha)).thenReturn(List.of(cita(EstadoCita.CANCELADA)));
        when(repo.save(any(Cita.class))).thenAnswer(inv -> { Cita c = inv.getArgument(0); c.setId(6L); return c; });
        assertEquals(EstadoCita.PROGRAMADA, sut.crear(request()).getEstado());
    }
    @Test void noPermiteReactivarAtendida() {
        when(repo.findById(5L)).thenReturn(Optional.of(cita(EstadoCita.ATENDIDA)));
        assertThrows(ReglaNegocioException.class, () -> sut.cambiarEstado(5L, EstadoCita.PROGRAMADA));
        verify(repo, never()).save(any());
    }
    @Test void noPermiteCancelarAtendida() {
        when(repo.findById(5L)).thenReturn(Optional.of(cita(EstadoCita.ATENDIDA)));
        assertThrows(ReglaNegocioException.class, () -> sut.cancelar(5L));
    }
    @Test void noPermiteEditarCancelada() {
        when(repo.findById(5L)).thenReturn(Optional.of(cita(EstadoCita.CANCELADA)));
        assertThrows(ReglaNegocioException.class, () -> sut.actualizar(5L, request()));
    }
    @Test void permiteAtenderUnaProgramada() {
        when(repo.findById(5L)).thenReturn(Optional.of(cita(EstadoCita.PROGRAMADA)));
        when(repo.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
        assertEquals(EstadoCita.ATENDIDA, sut.cambiarEstado(5L, EstadoCita.ATENDIDA).getEstado());
    }
}
