package com.ignaziopicciche.albergo.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd", style = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date dataInizio;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd", style = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date dataFine;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "customerId", insertable = false, updatable = false),
            @JoinColumn(name = "paymentMethodId", insertable = false, updatable = false),
            @JoinColumn(name = "id", insertable = false, updatable = false)
    })
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_PRENOTAZIONE_HOTEL")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "ID_PRENOTAZIONE_STANZA")
    private Stanza stanza;


    @ManyToMany(mappedBy = "prenotazioni")
    private List<Servizio> servizi;
}
