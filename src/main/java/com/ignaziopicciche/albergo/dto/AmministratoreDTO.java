package com.ignaziopicciche.albergo.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ignaziopicciche.albergo.model.Amministratore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmministratoreDTO {

    public Long id;
    public String nome;
    public String cognome;
    public String username;
    public String password;
    public Long idHotel;

    public AmministratoreDTO(Amministratore a){
        this.id = a.getId();
        this.nome = a.getNome();
        this.cognome = a.getCognome();
        this.username = a.getUsername();
        this.password = a.getPassword();
        this.idHotel = a.getHotel().getId();
    }

    @JsonIgnore
    @JsonProperty(value = "password")
    public String getPassword() {
        return password;
    }
}
