package com.businessapi.services;

import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.request.ProductCategorySaveRequestDTO;
import com.businessapi.dto.request.ProductCategoryUpdateRequestDTO;
import com.businessapi.entities.ProductCategory;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import com.businessapi.repositories.ProductCategoryRepository;
import com.businessapi.util.SessionManager;
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
        productCategoryRepository.save(ProductCategory.builder().memberId(SessionManager.memberId).name(dto.name()).build());
        return true;
    }
    public Boolean saveForDemoData(ProductCategorySaveRequestDTO dto)
    {
        productCategoryRepository.save(ProductCategory.builder().memberId(1L).name(dto.name()).build());
        return true;
    }

    public Boolean delete(Long id)
    {
        ProductCategory productCategory = productCategoryRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_CATEGORY_NOT_FOUND));
        SessionManager.authorizationCheck(productCategory.getMemberId());
        return true;
    }

    public Boolean update(ProductCategoryUpdateRequestDTO dto)
    {
        ProductCategory productCategory = productCategoryRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_CATEGORY_NOT_FOUND));
        SessionManager.authorizationCheck(productCategory.getMemberId());
        if (dto.name() != null)
        {
            productCategory.setName(dto.name());
        }

        productCategoryRepository.save(productCategory);
        return true;
    }

    public List<ProductCategory> findAll(PageRequestDTO dto)
    {
        return productCategoryRepository.findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(dto.searchText(),SessionManager.memberId, EStatus.DELETED, PageRequest.of(dto.page(), dto.size()));
    }

    public ProductCategory findById(Long id)
    {
        ProductCategory productCategory = productCategoryRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_CATEGORY_NOT_FOUND));
        SessionManager.authorizationCheck(productCategory.getMemberId());
        return   productCategory;
    }

    public ProductCategory findByIdForDemoData(Long id)
    {
        return productCategoryRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.PRODUCT_CATEGORY_NOT_FOUND));
    }
}
