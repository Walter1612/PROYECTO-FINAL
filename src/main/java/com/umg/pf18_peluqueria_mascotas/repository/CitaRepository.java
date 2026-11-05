package com.umg.pf18_peluqueria_mascotas.repository;

import com.umg.pf18_peluqueria_mascotas.entity.Cita;
import com.umg.pf18_peluqueria_mascotas.entity.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByEstado(EstadoCita estado);

    List<Cita> findByFechaHoraBetweenOrderByFechaHoraAsc(
            LocalDateTime inicio,
            LocalDateTime fin
    );

    boolean existsByFechaHoraAndEstadoNot(
            LocalDateTime fechaHora,
            EstadoCita estado
    );

    @Query("""
            SELECT c.servicio.nombre, COUNT(c)
            FROM Cita c
            WHERE c.estado <> :estadoCancelado
            GROUP BY c.servicio.id, c.servicio.nombre
            ORDER BY COUNT(c) DESC
            """)
    List<Object[]> obtenerServiciosMasSolicitados(
            @Param("estadoCancelado") EstadoCita estadoCancelado
    );

    @Query("""
            SELECT COALESCE(SUM(c.servicio.precio), 0)
            FROM Cita c
            WHERE c.fechaHora >= :inicio
              AND c.fechaHora < :fin
              AND c.estado <> :estadoCancelado
            """)
    Object obtenerTotalEstimadoDelDia(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("estadoCancelado") EstadoCita estadoCancelado
    );
}