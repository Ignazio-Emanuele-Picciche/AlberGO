package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
