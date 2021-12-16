package com.ignaziopicciche.albergo.service;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.exception.enums.ClienteEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.helper.ClienteHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClienteService {

    private final ClienteHelper clienteHelper;

    private static ClienteEnum clienteEnum;

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

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_EF");
        throw new ApiRequestException(clienteEnum.getMessage());
    }

    public Boolean delete(Long idCliente) {
        Preconditions.checkArgument(!Objects.isNull(idCliente));

        return clienteHelper.delete(idCliente);
    }

    public ClienteDTO update(ClienteDTO clienteDTO) {
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.id));
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.cognome));
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.telefono));

        return clienteHelper.update(clienteDTO);
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
