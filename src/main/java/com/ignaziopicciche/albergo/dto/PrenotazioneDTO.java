package com.ignaziopicciche.albergo.dto;

import com.ignaziopicciche.albergo.model.Prenotazione;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneDTO {
    public Long id;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd", style = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    public Date dataInizio;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd", style = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    public Date dataFine;

    public Long idCliente;
    public Long idHotel;
    public Long idStanza;

    public PrenotazioneDTO(Prenotazione p) {
        this.id = p.getId();
        this.dataInizio = p.getDataInizio();
        this.dataFine = p.getDataFine();
        this.idCliente = p.getCliente().getEmbeddedId().getId();
        this.idHotel = p.getHotel().getId();
        this.idStanza = p.getStanza().getId();
    }
}
