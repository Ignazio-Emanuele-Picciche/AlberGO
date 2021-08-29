package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;


    @GetMapping("/dettaglio")
    @ResponseBody
    public HotelDTO findById(@RequestParam(name = "idHotel") Long id){
        return hotelService.findById(id);
    }

    @PostMapping("/create")
    @ResponseBody
    public HotelDTO create(HotelDTO hotelDTO){
        return hotelService.create(hotelDTO);
    }

    @PutMapping("/update")
    @ResponseBody
    public HotelDTO update(HotelDTO hotelDTO){
        return hotelService.update(hotelDTO);
    }
}
