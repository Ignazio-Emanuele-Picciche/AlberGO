package com.ignaziopicciche.albergo.dto;

/*
Nome e cognome cliente
Data inizio e fine prenotazione
Numero stanza
Telefono cliente
 */

import com.ignaziopicciche.albergo.model.Categoria;
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
public class PrenotazioneClienteStanzaCategoriaDTO {
    
    public String nome;
    public String cognome;
    public Date dataInizio;
    public Date dataFine;
    public Integer numeroStanza;
    public String telefono;
    public Long idPrenotazione; // -> public Prenotazione prenotazione
    //public Long nomeHotel
    public String nomeCategoria;
    public String documento;

    /*
    Va cercato per cliente.idCliente

    public Prenotazione prenotazione;
    public Hotel hotel;
    public Cliente cliente;
    public Stanza stanza;
    public Cetegoria categoria
     */

    public PrenotazioneClienteStanzaCategoriaDTO(Prenotazione p, Cliente cli, Stanza s, Categoria cat){
        this.nome = cli.getNome();
        this.cognome = cli.getCognome();
        this.documento = cli.getDocumento();
        this.telefono = cli.getTelefono();

        this.dataInizio = p.getDataInizio();
        this.dataFine = p.getDataFine();
        this.idPrenotazione = p.getId();

        this.numeroStanza = s.getNumeroStanza();

        this.nomeCategoria = cat.getNome();
    }
}
