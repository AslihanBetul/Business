package com.stockservice.services;

import com.stockservice.dto.request.PageRequestDTO;
import com.stockservice.dto.request.ProductSaveRequestDTO;
import com.stockservice.dto.request.ProductUpdateRequestDTO;
import com.stockservice.entities.Product;
import com.stockservice.entities.enums.EStatus;
import com.stockservice.exception.ErrorType;
import com.stockservice.exception.StockServiceException;
import com.stockservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;

    public Product findById(Long id){

        return productRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_NOT_FOUND));
    }

    public Boolean save(ProductSaveRequestDTO dto)
    {
        productCategoryService.findById(dto.productCategoryId());
        productRepository.save(Product
                .builder()
                .productCategoryId(dto.productCategoryId())
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stockCount(dto.stockCount())
                .minimumStockLevel(dto.minimumStockLevel())
                .build());
        return true;
    }

    public Boolean delete(Long id)
    {
        Product product = productRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_NOT_FOUND));
        product.setStatus(EStatus.DELETED);
        productRepository.save(product);
        return true;
    }

    public Boolean update(ProductUpdateRequestDTO dto)
    {
        Product product = productRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_NOT_FOUND));
        if (dto.productCategoryId() != null)
        {
            product.setProductCategoryId(dto.productCategoryId());
        }
        if (dto.name() != null)
        {
            product.setName(dto.name());
        }
        if (dto.description() != null)
        {
            product.setDescription(dto.description());
        }
        if (dto.price() != null)
        {
            product.setPrice(dto.price());
        }
        if (dto.stockCount() != null)
        {
            product.setStockCount(dto.stockCount());
        }
        if (dto.minimumStockLevel() != null)
        {
            product.setMinimumStockLevel(dto.minimumStockLevel());
        }
        productRepository.save(product);
        return true;
    }

    public List<Product> findAll(PageRequestDTO dto)
    {
        return productRepository.findAllByNameContaining(dto.searchText(), PageRequest.of(dto.page(), dto.size()));
    }
}
