package com.ignaziopicciche.albergo;

import com.ignaziopicciche.albergo.helper.AutenticazioneHelper;
import com.ignaziopicciche.albergo.service.AutenticazioneService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class AlberGoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlberGoApplication.class, args);
    }
}
