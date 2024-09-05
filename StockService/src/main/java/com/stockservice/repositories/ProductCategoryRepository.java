package com.stockservice.repositories;


import com.stockservice.entities.Product;
import com.stockservice.entities.ProductCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>
{
    List<ProductCategory> findAllByNameContaining(String s, PageRequest of);
}
