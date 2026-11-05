package com.umg.pf18_peluqueria_mascotas.repository;

import com.umg.pf18_peluqueria_mascotas.entity.ServicioEstetica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicioEsteticaRepository extends JpaRepository<ServicioEstetica, Long> {

    List<ServicioEstetica> findByActivoTrue();

    Optional<ServicioEstetica> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}