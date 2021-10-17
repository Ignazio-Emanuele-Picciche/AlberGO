package com.ignaziopicciche.albergo.service;

import com.google.common.base.Preconditions;
import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.helper.ClienteHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClienteService {

    @Autowired
    private ClienteHelper clienteHelper;

    public Long create(ClienteDTO clienteDTO) {
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.cognome));
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.documento));
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.telefono));
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.username));
        Preconditions.checkArgument(!Objects.isNull(clienteDTO.password));

        return clienteHelper.create(clienteDTO);
    }

    public Boolean delete(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return clienteHelper.delete(id);
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

    public List<ClienteDTO> findAllByNomeCognome(String nome, String cognome) {
        nome = StringUtils.isNotBlank(nome) ? nome : null;
        cognome = StringUtils.isNotBlank(cognome) ? cognome : null;
        return clienteHelper.findAllByNomeCognome(nome, cognome);
    }

}
