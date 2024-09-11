package com.businessapi.services;

import com.businessapi.dto.request.OrderSaveRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.request.ProductSaveRequestDTO;
import com.businessapi.dto.request.ProductUpdateRequestDTO;
import com.businessapi.entities.Product;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import com.businessapi.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private OrderService orderService;

    @Autowired
    public void setServices(@Lazy OrderService orderService) {
        this.orderService = orderService;
    }

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
        return productRepository.findAllByNameContainingIgnoreCase(dto.searchText(), PageRequest.of(dto.page(), dto.size()));
    }

    public List<Product> findAllByMinimumStockLevel(PageRequestDTO dto)
    {
        List<Product> productList = productRepository.findAllByMinimumStockLevelAndStatusAndNameContainingIgnoreCase(EStatus.ACTIVE, dto.searchText(), PageRequest.of(dto.page(), dto.size()));

        productList.forEach(product -> {
            if (!product.getIsProductAutoOrdered())
            {
                //TODO WILL CONSTRUCT NEW SYSTEM TO AUTO ORDER STOCK
            }
        });
        return productRepository.findAllByMinimumStockLevelAndStatusAndNameContainingIgnoreCase(EStatus.ACTIVE, dto.searchText(), PageRequest.of(dto.page(), dto.size()));
    }
}
