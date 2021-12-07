package com.ignaziopicciche.albergo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ignaziopicciche.albergo.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    public Long id;
    public String nome;
    public String cognome;
    public String telefono;
    public String documento;
    public String username;
    public String password;

    public ClienteDTO(Cliente c) {
        this.id = c.getId();
        this.nome = c.getNome();
        this.cognome = c.getCognome();
        this.telefono = c.getTelefono();
        this.documento = c.getDocumento();
        this.username = c.getUsername();
        //this.password = c.getPassword();
    }
}
