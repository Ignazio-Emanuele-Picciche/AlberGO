package com.ignaziopicciche.albergo.dto;

import com.ignaziopicciche.albergo.model.Stanza;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StanzaDTO {
    public Long id;
    public Integer numeroStanza;
    public Boolean fuoriServizio;
    public String descrizione;
    public Integer metriQuadri;

    public Long idHotel;
    public Long idCategoria;

    public StanzaDTO(Stanza s) {
        this.id = s.getId();
        this.numeroStanza = s.getNumeroStanza();
        this.fuoriServizio = s.getFuoriServizio();
        this.descrizione = s.getDescrizione();
        this.metriQuadri = s.getMetriQuadri();
        this.idHotel = s.getHotel().getId();
        this.idCategoria = s.getCategoria().getId();
    }
}
