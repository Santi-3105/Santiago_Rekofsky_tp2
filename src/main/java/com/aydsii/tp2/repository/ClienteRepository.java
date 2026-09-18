package com.aydsii.tp2.repository;

import com.aydsii.tp2.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    boolean existsByEmail(String email);
}