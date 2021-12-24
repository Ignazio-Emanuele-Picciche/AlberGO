package com.ignaziopicciche.albergo.service;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.exception.enums.ClienteEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.helper.ClienteHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
    Nella classe ClienteService sono presenti i metodi che controllano che i dati passati dalla classe
    ClienteController non siano nulli, in generale controllare che i dati obbligatori non siano nulli o vuoti.
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
@Slf4j
public class ClienteService {

    private final ClienteHelper clienteHelper;

    private static ClienteEnum clienteEnum;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo clienteHelper,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe ClienteHelper.
     * @param clienteHelper
     */
    public ClienteService(ClienteHelper clienteHelper) {
        this.clienteHelper = clienteHelper;
    }

    public Long create(ClienteDTO clienteDTO) throws Exception {

        if (StringUtils.isNotBlank(clienteDTO.nome) &&
                StringUtils.isNotBlank(clienteDTO.cognome) &&
                StringUtils.isNotBlank(clienteDTO.documento) &&
                StringUtils.isNotBlank(clienteDTO.telefono) &&
                StringUtils.isNotBlank(clienteDTO.username) &&
                StringUtils.isNotBlank(clienteDTO.password)) {
            return clienteHelper.create(clienteDTO);
        }

        log.error("Non sono stati compilati dei campi obbligatori");
        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_EF");
        throw new ApiRequestException(clienteEnum.getMessage());
    }

    public ClienteDTO findById(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return clienteHelper.findById(id);
    }

    public List<ClienteDTO> findAll(Long idHotel) {
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return clienteHelper.findAll(idHotel);
    }

    public List<ClienteDTO> findAllByNomeCognome(String nome, String cognome, Long idHotel) {
        nome = StringUtils.isNotBlank(nome) ? nome : null;
        cognome = StringUtils.isNotBlank(cognome) ? cognome : null;
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        return clienteHelper.findAllByNomeCognome(nome, cognome, idHotel);
    }

    public ClienteDTO findClienteByUsername(String username) {
        Preconditions.checkArgument(!Objects.isNull(username));

        return clienteHelper.findClienteByUsername(username);
    }

}
