package com.ignaziopicciche.albergo.dto;

import com.ignaziopicciche.albergo.model.Servizio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServizioDTO {

    public Long id;
    public String nome;
    public Double prezzo;
    public Long idHotel;
    public Long idPrenotazione;

    public ServizioDTO(Servizio servizio){
        this.id = servizio.getId();
        this.nome = servizio.getNome();
        this.prezzo = servizio.getPrezzo();
        this.idHotel = servizio.getHotel().getId();
    }


}
