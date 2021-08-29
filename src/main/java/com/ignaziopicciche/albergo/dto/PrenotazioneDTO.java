package com.ignaziopicciche.albergo.dto;

import com.ignaziopicciche.albergo.model.Prenotazione;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneDTO {
    public Long id;
    public LocalDate dataInizio;
    public LocalDate dataFine;

    public Long idCliente;
    public Long idHotel;
    public Long idStanza;

    public PrenotazioneDTO(Prenotazione p) {
        this.id = p.getId();
        this.dataInizio = p.getDataInizio();
        this.dataFine = p.getDataFine();
        this.idCliente = p.getCliente().getId();
        this.idHotel = p.getHotel().getId();
        this.idStanza = p.getStanza().getId();
    }
}
