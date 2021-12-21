package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    -Nella classe ClienteController vengono gestiti ed organizzati tutti gli endpoint relativi al cliente.
    -I path delle api, ovvero delle attività che si possono svolgere relative al cliente, iniziano con:
    "http://localhost:8080/api/cliente/...".
    -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe ClienteService
    per il controllo e la validità dei dati in input delle request dal front-end.
    -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * In questo metodo viene implmenetata la logica dell'annotazione @Autowired per l'attributo clienteService,
     * ovvero stiamo chiedendo a Spring di invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe ClienteService.
     * @param clienteService
     */
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Enpoint che restituisce un cliente associato all'idCliente passato in input
     * @param id
     * @return ClienteDTO
     */
    @GetMapping("/dettaglio")
    public ClienteDTO findById(@RequestParam(name = "idCliente") Long id) {
        return clienteService.findById(id);
    }

    /**
     * Endpoint che ritorna tutti i clienti registrati in uno specifico hotel
     * @param idHotel
     * @return List<ClienteDTO>
     */
    @GetMapping("/lista")
    public List<ClienteDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return clienteService.findAll(idHotel);
    }

    //TODO rimuovere
    @PutMapping("/update")
    public ClienteDTO update(@RequestBody ClienteDTO clienteDTO) {
        return clienteService.update(clienteDTO);
    }

    //TODO rimuovere
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idCliente") Long idCliente) {
        return clienteService.delete(idCliente);
    }

    /**
     * Endpoint che restituisce tutti i clienti che iniziano per nome e cognome di un hotel
     * @param nome
     * @param cognome
     * @param idHotel
     * @return List<ClienteDTO>
     */
    //find all Cienti startWith nome or cognome
    //TODO idHOtel non ha senso visto che i clienti si troano in tutti gli hotel
    @GetMapping("/searchNomeCognome")
    public List<ClienteDTO> findAllByNomeCognome(@RequestParam("nome") String nome, @RequestParam("cognome") String cognome, @RequestParam("idHotel") Long idHotel) {
        return clienteService.findAllByNomeCognome(nome, cognome, idHotel);
    }

    /**
     * Endpoint per la registrazione di un nuovo cliente
     * @param clienteDTO
     * @return ClienteDTO
     * @throws Exception
     */
    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody ClienteDTO clienteDTO) throws Exception {
        return ResponseEntity.ok(clienteService.create(clienteDTO));
    }

    /**
     * Endpoint che restituisce un cliente cercato per username
     * @param username
     * @return ClienteDTO
     */
    @GetMapping("/dettaglio/username")
    public ClienteDTO findClienteByUsername(@RequestParam("username") String username){
        return clienteService.findClienteByUsername(username);
    }


}
