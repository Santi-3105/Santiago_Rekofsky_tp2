package com.aydsii.tp2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 2, message = "debe tener al menos 2 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacio")
    @Size(min = 2, message = "debe tener al menos 2 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "debe ser un email valido")
    private String email;

    // Opcional: si viene, solo puede contener digitos
    @Pattern(regexp = "^\\d*$", message = "solo debe contener digitos")
    private String telefono;

    public ClienteDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}