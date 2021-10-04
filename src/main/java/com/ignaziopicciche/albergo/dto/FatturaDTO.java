package com.ignaziopicciche.albergo.dto;

/*
Nome e cognome cliente
Data inizio e fine prenotazione
Numero stanza
Telefono cliente
 */

import com.ignaziopicciche.albergo.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FatturaDTO {
    
    /*
    Va cercato per cliente.idCliente
    */

    public PrenotazioneDTO prenotazione;
    public HotelDTO hotel;
    public ClienteDTO cliente;
    public StanzaDTO stanza;
    public CategoriaDTO categoria;


    public FatturaDTO(Prenotazione prenotazione, Cliente cliente, Stanza stanza, Categoria categoria, Hotel hotel) {
        this.prenotazione = new PrenotazioneDTO(prenotazione);
        this.hotel = new HotelDTO(hotel);
        this.cliente = new ClienteDTO(cliente);
        this.stanza = new StanzaDTO(stanza);
        this.categoria = new CategoriaDTO(categoria);
    }
}
