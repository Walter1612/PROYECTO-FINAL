package com.umg.pf18_peluqueria_mascotas;

import com.umg.pf18_peluqueria_mascotas.dto.ServicioEsteticaRequest;
import com.umg.pf18_peluqueria_mascotas.entity.ServicioEstetica;
import com.umg.pf18_peluqueria_mascotas.exception.RecursoDuplicadoException;
import com.umg.pf18_peluqueria_mascotas.repository.ServicioEsteticaRepository;
import com.umg.pf18_peluqueria_mascotas.service.ServicioEsteticaService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioEsteticaServiceTest {
    @Test void rechazaNombreDuplicado() {
        ServicioEsteticaRepository repo = mock(ServicioEsteticaRepository.class);
        ServicioEsteticaService sut = new ServicioEsteticaService(repo);
        ServicioEsteticaRequest r = new ServicioEsteticaRequest();
        r.setNombre("Baño"); r.setPrecio(new BigDecimal("70.00")); r.setDuracionMinutos(30);
        when(repo.existsByNombreIgnoreCase("Baño")).thenReturn(true);
        assertThrows(RecursoDuplicadoException.class, () -> sut.crear(r));
        verify(repo, never()).save(any());
    }
    @Test void desactivaSinBorrarRegistros() {
        ServicioEsteticaRepository repo = mock(ServicioEsteticaRepository.class);
        ServicioEsteticaService sut = new ServicioEsteticaService(repo);
        ServicioEstetica s = new ServicioEstetica(); s.setId(1L); s.setActivo(true);
        when(repo.findById(1L)).thenReturn(Optional.of(s));
        sut.desactivar(1L);
        assertFalse(s.getActivo());
        verify(repo).save(s);
        verify(repo, never()).delete(any());
    }
}
