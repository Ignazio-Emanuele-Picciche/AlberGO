package com.ignaziopicciche.albergo.dto;

import com.ignaziopicciche.albergo.model.Hotel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {
    public Long id;
    public String nome;
    public String indirizzo;
    public Integer stelle;
    public String descrizione;
    public String telefono;
    public String codiceHotel;
    public String publicKey;

    public HotelDTO(Hotel h) {
        this.id = h.getId();
        this.nome = h.getNome();
        this.indirizzo = h.getIndirizzo();
        this.stelle = h.getStelle();
        this.descrizione = h.getDescrizione();
        this.telefono = h.getTelefono();
        this.codiceHotel = h.getCodiceHotel();
    }
}
