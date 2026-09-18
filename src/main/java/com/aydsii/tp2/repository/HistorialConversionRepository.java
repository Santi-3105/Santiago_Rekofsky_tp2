package com.aydsii.tp2.repository;

import com.aydsii.tp2.entity.HistorialConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialConversionRepository extends JpaRepository<HistorialConversion, Integer> {

    List<HistorialConversion> findByMonedaOrigenAndMonedaDestinoOrderByFechaConsultaDesc(
            String monedaOrigen, String monedaDestino);
}