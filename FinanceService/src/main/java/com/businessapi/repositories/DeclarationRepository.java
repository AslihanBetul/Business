package com.businessapi.repositories;

import com.businessapi.entity.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

}
