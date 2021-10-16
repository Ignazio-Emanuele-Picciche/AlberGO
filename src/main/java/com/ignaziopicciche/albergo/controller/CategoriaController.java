package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ResponseBody
    public CategoriaDTO findById(@RequestParam(name = "idCategoria") Long id) {
        return categoriaService.findById(id);
    }

    @GetMapping("/lista")
    public List<CategoriaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return categoriaService.findAll(idHotel);
    }

    @PutMapping("/update")
    public Long update(@RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.update(categoriaDTO);
    }

    @PostMapping("/create")
    public Long create(@RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.create(categoriaDTO);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idCategoria") Long id) {
        return categoriaService.delete(id);
    }


}
