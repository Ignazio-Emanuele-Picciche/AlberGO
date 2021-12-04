package com.ignaziopicciche.albergo.controller;


import com.ignaziopicciche.albergo.dto.AmministratoreDTO;
import com.ignaziopicciche.albergo.service.AmministratoreService;
import com.ignaziopicciche.albergo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/amministratore")
public class AmministratoreController {

    private final AmministratoreService amministratoreService;

    public AmministratoreController(AmministratoreService amministratoreService) {
        this.amministratoreService = amministratoreService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody AmministratoreDTO amministratoreDTO) {
        return ResponseEntity.ok(amministratoreService.create(amministratoreDTO));
    }


}
