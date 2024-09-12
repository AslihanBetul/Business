package com.businessapi.repositories;


import com.businessapi.entities.ProductCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>
{
    List<ProductCategory> findAllByNameContainingIgnoreCase(String s, PageRequest of);
}
