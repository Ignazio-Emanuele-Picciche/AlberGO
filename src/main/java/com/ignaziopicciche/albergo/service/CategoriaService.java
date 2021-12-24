package com.ignaziopicciche.albergo.service;


import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.helper.CategoriaHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
    Nella classe CategoriaService sono presenti i metodi che controllano che i dati passati dalla classe
    CategoriaController non siano nulli, in generale controllare che i dati obbligatori non siano nulli o vuoti.
    Nel caso in cui non fossero nulli, i dati dal livello "service" verranno passati al livello "helper" che si occuperà
    dell'implementazione della logica, ovvero le operazioni, del metodo.
    Nel caso in cui, invece, qualche dato obbligatorio non fosse stato compilato, viene restituita un'eccezione nei log
    del back-end.
    Per il controllo dei campi viene usato il metodo checkArgument() della classe Preconditions (fornito dalla dependency
    Guava Preconditions), ponendo il campo obbligatorio diverso da null.

    In generale:
    Preconditions.checkArgument(!Objects.isNull("campo obbligatorio"));
 */

@Service
public class CategoriaService {

    private final CategoriaHelper categoriaHelper;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo categoriaHelper,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe CategoriaHelper.
     * @param categoriaHelper
     */
    public CategoriaService(CategoriaHelper categoriaHelper) {
        this.categoriaHelper = categoriaHelper;
    }

    public Long create(CategoriaDTO categoriaDTO){
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.prezzo));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.giorniPenale));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.giorniBlocco));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.qtaPenale));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.idHotel));

        return categoriaHelper.create(categoriaDTO);
    }


    public boolean delete(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return categoriaHelper.delete(id);
    }


    public CategoriaDTO findById(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return categoriaHelper.findById(id);
    }

    public Long update(CategoriaDTO categoriaDTO){
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.id));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.giorniPenale));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.giorniBlocco));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.qtaPenale));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.prezzo));

        return categoriaHelper.update(categoriaDTO);
    }


    public List<CategoriaDTO> findAll(Long idHotel){
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return categoriaHelper.findAll(idHotel);
    }

    public List<CategoriaDTO> findAllByNome(String nome, Long idHotel) {
        Preconditions.checkArgument(!Objects.isNull(nome));
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        return categoriaHelper.findAllByNome(nome, idHotel);
    }

}
