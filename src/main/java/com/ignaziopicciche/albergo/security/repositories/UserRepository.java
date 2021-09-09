package com.ignaziopicciche.albergo.security.repositories;

import com.ignaziopicciche.albergo.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>{

    User findByUsername(String userName);

}
