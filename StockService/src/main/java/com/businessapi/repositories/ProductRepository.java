package com.businessapi.repositories;


import com.businessapi.entities.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findAllByNameContaining(String s, PageRequest of);
}
