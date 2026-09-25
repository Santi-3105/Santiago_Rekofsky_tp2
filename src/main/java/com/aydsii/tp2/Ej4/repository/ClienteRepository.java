package com.aydsii.tp2.Ej4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aydsii.tp2.Ej4.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    boolean existsByEmail(String email);
}