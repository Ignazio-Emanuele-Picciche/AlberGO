package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.dto.AmministratoreDTO;

public interface AmministratoreService {
    Long create(AmministratoreDTO amministratoreDTO);

    AmministratoreDTO findAmministratoreByUsername(String username);
}
