package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.service.HotelService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }



    @GetMapping("/dettaglio")
    public HotelDTO findById(@RequestParam(name = "idHotel") Long id){
        return hotelService.findById(id);
    }

    @PostMapping("/create")
    public HotelDTO create(@RequestBody HotelDTO hotelDTO) throws Exception {
        return hotelService.create(hotelDTO);
    }

    //TODO non si puo fare l'update di hotel, non si puo cambiare niente
    /*@PutMapping("/update")
    public HotelDTO update(@RequestBody HotelDTO hotelDTO){
        return hotelService.update(hotelDTO);
    }*/


    //Trova gli hotel con il nome che inizia per...
    @GetMapping("/searchNome")
    public List<HotelDTO> findHotelByName(@RequestParam("nomeHotel") String nomeHotel){
        return hotelService.findHotelByName(nomeHotel);
    }

    //Trova gli hotel che iniziano con l'indirizzo
    @GetMapping("/searchIndirizzo")
    public List<HotelDTO> findHotelByIndirizzo(@RequestParam("indirizzoHotel") String indirizzoHotel){
        return hotelService.findHotelByIndirizzo(indirizzoHotel);
    }
    

    @GetMapping("/allhotel")
    public List<HotelDTO> getAllHotel(){
        return hotelService.getAllHotel();
    }

    @GetMapping("/hotelByCliente")
    public List<HotelDTO> findHotelByClienteId(@RequestParam("idCliente") Long idCliente){
        return hotelService.findHotelByClienteId(idCliente);
    }


}
