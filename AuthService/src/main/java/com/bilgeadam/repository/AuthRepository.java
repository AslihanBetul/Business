package com.bilgeadam.repository;

import com.bilgeadam.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository  extends JpaRepository<Auth,Long> {
    boolean existsByEmail(String email);



    Optional<Auth> findOptionalByEmail(String email);
}
