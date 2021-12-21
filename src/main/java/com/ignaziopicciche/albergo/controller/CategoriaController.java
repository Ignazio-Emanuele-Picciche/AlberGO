package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    -Nella classe CategoriaController vengono gestiti ed organizzati tutti gli endpoint relativi alla categoria.
    -I path delle api, ovvero delle attività che si possono svolgere relative alla categoria, iniziano con:
    "http://localhost:8080/api/categoria/...".
    -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe CategoriaService
    per il controllo e la validità dei dati in input delle request dal front-end.
    -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */
@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * In questo metodo viene implmenetata la logica dell'annotazione @Autowired per l'attributo categoriaService,
     * ovvero stiamo chiedendo a Spring di invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe CategoriaService.
     * @param categoriaService
     */
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }


    /**
     * Endpoint che, tramite un id in input, ritorna la categoria associata all'id
     * @param id
     * @return CategoriaDTO
     */
    @GetMapping("/dettaglio")
    public CategoriaDTO findById(@RequestParam(name = "idCategoria") Long id) {
        return categoriaService.findById(id);
    }

    /**
     * Endpoint che, tramite un idHotel in input, ritorna la lista delle categorie presenti
     * in quell'hotel
     * @param idHotel
     * @return List<CategoriaDTO>
     */
    @GetMapping("/lista")
    public List<CategoriaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return categoriaService.findAll(idHotel);
    }

    /**
     * Endpoint per l'update dei dati di una categoria già presente nel sistema
     * @param categoriaDTO
     * @return id categoria
     */
    @PutMapping("/update")
    public Long update(@RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.update(categoriaDTO);
    }

    /**
     * Endpoint per la creazione di una nuova categoria
     * @param categoriaDTO
     * @return id categoria
     */
    @PostMapping("/create")
    public Long create(@RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.create(categoriaDTO);
    }

    /**
     * Endpoint per l'liminazione di una categoria
     * @param id
     * @return Boolean (true se tutto è andato a buon fine)
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idCategoria") Long id) {
        return categoriaService.delete(id);
    }

    /**
     * Enpoint che cerca tutte le categorie di un hotel, che iniziano per nome
     * @param nome
     * @param idHotel
     * @return List<CategoriaDTO>
     */
    @GetMapping("/searchNome")
    public List<CategoriaDTO> findAllByNome(@RequestParam("nome") String nome, @RequestParam("idHotel") Long idHotel) {
        return categoriaService.findAllByNome(nome, idHotel);
    }


}
