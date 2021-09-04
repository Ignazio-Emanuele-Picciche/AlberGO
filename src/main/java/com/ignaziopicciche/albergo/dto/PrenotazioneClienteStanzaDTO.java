package com.ignaziopicciche.albergo.dto;

/*
Nome e cognome cliente
Data inizio e fine prenotazione
Numero stanza
Telefono cliente
 */

import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.Prenotazione;
import com.ignaziopicciche.albergo.model.Stanza;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrenotazioneClienteStanzaDTO {
    
    public String nome;
    public String cognome;
    public Date dataInizio;
    public Date dataFine;
    public Integer numeroStanza;
    public String telefono;
    public Long idPrenotazione;

    public PrenotazioneClienteStanzaDTO(Prenotazione p, Cliente c, Stanza s){
        this.nome = c.getNome();
        this.cognome = c.getCognome();
        this.dataInizio = p.getDataInizio();
        this.dataFine = p.getDataFine();
        this.numeroStanza = s.getNumeroStanza();
        this.telefono = c.getTelefono();
        this.idPrenotazione = p.getId();
    }
}
