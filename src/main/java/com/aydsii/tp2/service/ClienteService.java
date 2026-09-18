package com.aydsii.tp2.service;

import com.aydsii.tp2.dto.ClienteDTO;
import com.aydsii.tp2.exception.BadRequestException;
import com.aydsii.tp2.model.Cliente;
import com.aydsii.tp2.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /*
     Alta simple: sin chequeo de email duplicado, se apoya solo
     en la restriccion UNIQUE de la base (fallaria con un error de BD si se repite).
     */
    public Cliente altaSimple(ClienteDTO dto) {
        Cliente cliente = new Cliente(dto.getNombre(), dto.getApellido(), dto.getEmail(), dto.getTelefono());
        return clienteRepository.save(cliente);
    }

    /*
     Alta con validacion: ademas de Bean Validation (aplicada en el controller),
     verifica que el email no este ya registrado antes de insertar.
     */
    public Cliente altaValidada(ClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("El email ya esta registrado");
        }
        Cliente cliente = new Cliente(dto.getNombre(), dto.getApellido(), dto.getEmail(), dto.getTelefono());
        return clienteRepository.save(cliente);
    }
}