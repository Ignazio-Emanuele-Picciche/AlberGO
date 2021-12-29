package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.service.HotelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * -Nella classe HotelController vengono gestiti e organizzati tutti gli endpoint relativi all'hotel.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'hotel, iniziano con:
 * "http://localhost:8080/api/hotel/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe HotelService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelService hotelService;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo hotelService,
     * ovvero stiamo chiedendo a Spring di invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe HotelService.
     *
     * @param hotelService
     */
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }


    /**
     * Endpoint che restituisce un hotel associato all'idHotel in input
     *
     * @param id
     * @return HotelDTO
     */
    @GetMapping("/dettaglio")
    public HotelDTO findById(@RequestParam(name = "idHotel") Long id) {
        return hotelService.findById(id);
    }

    /**
     * Endpoint per la creazione di un nuovo hotel
     *
     * @param hotelDTO
     * @return HotelDTO
     * @throws Exception
     */
    @PostMapping("/create")
    public HotelDTO create(@RequestBody HotelDTO hotelDTO) throws Exception {
        return hotelService.create(hotelDTO);
    }

    /**
     * Endpoint che restituisce tutti gli hotel con nome che iniziano per nomeHotel
     *
     * @param nomeHotel
     * @return List<HotelDTO>
     */
    @GetMapping("/searchNome")
    public List<HotelDTO> findHotelByName(@RequestParam("nomeHotel") String nomeHotel) {
        return hotelService.findHotelByName(nomeHotel);
    }

    /**
     * Endpoint che restituisce tutti gli hotel con indirizzo che inizia per indirizzohotel
     *
     * @param indirizzoHotel
     * @return List<HotelDTO>
     */
    @GetMapping("/searchIndirizzo")
    public List<HotelDTO> findHotelByIndirizzo(@RequestParam("indirizzoHotel") String indirizzoHotel) {
        return hotelService.findHotelByIndirizzo(indirizzoHotel);
    }

    /**
     * Endpoint che restituisce un hotel cercato per il suo codiceHotel
     *
     * @param codiceHotel
     * @return HotelDTO
     */
    @GetMapping("/searchCodiceHotel")
    public HotelDTO findHotelByCodiceHotel(@RequestParam("codiceHotel") String codiceHotel) {
        return hotelService.findHotelByCodiceHotel(codiceHotel);
    }

    /**
     * Endpoint che restituisce tutti gli hotel presenti nel sistema
     *
     * @return List<HotelDTO>
     */
    @GetMapping("/allhotel")
    public List<HotelDTO> getAllHotel() {
        return hotelService.getAllHotel();
    }

}
