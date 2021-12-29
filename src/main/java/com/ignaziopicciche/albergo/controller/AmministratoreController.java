package com.ignaziopicciche.albergo.controller;


import com.ignaziopicciche.albergo.dto.AmministratoreDTO;
import com.ignaziopicciche.albergo.service.AmministratoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * -Nella classe AmministratoreController vengono gestiti e organizzati tutti gli endpoint relativi all'amministratore.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/amministratore/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe AmministratoreService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

@RestController
@RequestMapping("/api/amministratore")
public class AmministratoreController {

    private final AmministratoreService amministratoreService;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo amministratoreService,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe AmministratoreService.
     *
     * @param amministratoreService
     */
    public AmministratoreController(AmministratoreService amministratoreService) {
        this.amministratoreService = amministratoreService;
    }

    /**
     * Enpoint per la registrazione di un nuovo amministratore
     *
     * @param amministratoreDTO
     * @return Id amministratore
     */
    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody AmministratoreDTO amministratoreDTO) {
        return ResponseEntity.ok(amministratoreService.create(amministratoreDTO));
    }

    /**
     * Enpoint per cercare un amministratore per username
     *
     * @param username
     * @return AmministratoreDTO
     */
    @GetMapping("/dettaglio/username")
    public AmministratoreDTO findAmministratoreByUsername(@RequestParam("username") String username) {
        return amministratoreService.findAmministratoreByUsername(username);
    }


}
