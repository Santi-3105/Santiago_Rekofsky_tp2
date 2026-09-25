package com.aydsii.tp2.Ej4.service;

import com.aydsii.tp2.BadRequestException;
import com.aydsii.tp2.Ej4.dto.ClienteDTO;
import com.aydsii.tp2.Ej4.model.Cliente;
import com.aydsii.tp2.Ej4.repository.ClienteRepository;

import org.springframework.stereotype.Service;


//Indica que es de tipo Serivce, es algo que se instanciara una sola vez y se utilize durante la ejecucion
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    //Inyeccion de dependencias. Crea una instancia de estos sin necesidad un New cuando arranca Spring
    //Ventaja de que ya la recirbe armada como parametro
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