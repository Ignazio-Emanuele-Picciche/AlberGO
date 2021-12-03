package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }


    @GetMapping("/dettaglio")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoriaDTO findById(@RequestParam(name = "idCategoria") Long id) {
        return categoriaService.findById(id);
    }

    @GetMapping("/lista")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public List<CategoriaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return categoriaService.findAll(idHotel);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long update(@RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.update(categoriaDTO);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long create(@RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.create(categoriaDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean delete(@RequestParam(name = "idCategoria") Long id) {
        return categoriaService.delete(id);
    }

    @GetMapping("/searchNome")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<CategoriaDTO> findAllByNome(@RequestParam("nome") String nome, @RequestParam("idHotel") Long idHotel) {
        return categoriaService.findAllByNome(nome, idHotel);
    }


}
