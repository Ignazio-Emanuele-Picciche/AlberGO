package com.ignaziopicciche.albergo.dto;

import com.ignaziopicciche.albergo.model.Categoria;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    public Long id;
    public String nome;
    public Double prezzo;
    public String descrizione;

    public Integer giorniPenale;
    public Double qtaPenale;
    public Integer giorniBlocco;

    public Long idHotel;


    public CategoriaDTO(Categoria c) {
        this.id = c.getId();
        this.nome = c.getNome();
        this.prezzo = c.getPrezzo();
        this.descrizione = c.getDescrizione();
        this.giorniPenale = c.getGiorniPenale();
        this.qtaPenale = c.getQtaPenale();
        this.giorniBlocco = c.getGiorniBlocco();
        this.idHotel = c.getHotel().getId();
    }

}
