package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
