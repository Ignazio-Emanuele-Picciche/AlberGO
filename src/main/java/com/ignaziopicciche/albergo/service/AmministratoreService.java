package com.ignaziopicciche.albergo.service;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.helper.AmministratoreHelper;
import com.ignaziopicciche.albergo.dto.AmministratoreDTO;
import com.ignaziopicciche.albergo.security.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;


//UserService
@Service
public class AmministratoreService {

    private final AmministratoreHelper amministratoreHelper;

    public AmministratoreService(AmministratoreHelper amministratoreHelper) {
        this.amministratoreHelper = amministratoreHelper;
    }


    public Long create(AmministratoreDTO amministratoreDTO) {
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.cognome));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.username));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.password));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.idHotel));

        return amministratoreHelper.create(amministratoreDTO);
    }


}
