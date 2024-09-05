package com.stockservice.services;

import com.stockservice.dto.request.PageRequestDTO;
import com.stockservice.dto.request.ProductCategorySaveRequestDTO;
import com.stockservice.dto.request.ProductCategoryUpdateRequestDTO;
import com.stockservice.entities.Product;
import com.stockservice.entities.ProductCategory;
import com.stockservice.exception.ErrorType;
import com.stockservice.exception.StockServiceException;
import com.stockservice.repositories.OrderRepository;
import com.stockservice.repositories.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService
{
    private final ProductCategoryRepository productCategoryRepository;

    public Boolean save(ProductCategorySaveRequestDTO dto)
    {
        productCategoryRepository.save(ProductCategory.builder().name(dto.name()).build());
        return true;
    }

    public Boolean delete(Long id)
    {
        productCategoryRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_CATEGORY_NOT_FOUND));
        return true;
    }

    public Boolean update(ProductCategoryUpdateRequestDTO dto)
    {
        ProductCategory productCategory = productCategoryRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_CATEGORY_NOT_FOUND));
        if (dto.name() != null)
        {
            productCategory.setName(dto.name());
        }

        productCategoryRepository.save(productCategory);
        return true;
    }

    public List<ProductCategory> findAll(PageRequestDTO dto)
    {
        return productCategoryRepository.findAllByNameContaining(dto.searchText(), PageRequest.of(dto.page(), dto.size()));
    }

    public ProductCategory findById(Long id)
    {
        return   productCategoryRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_CATEGORY_NOT_FOUND));
    }
}
