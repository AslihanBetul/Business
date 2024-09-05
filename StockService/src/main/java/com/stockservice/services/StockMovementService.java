package com.stockservice.services;

import com.stockservice.dto.request.PageRequestDTO;
import com.stockservice.dto.request.StockMovementSaveDTO;
import com.stockservice.dto.request.StockMovementUpdateRequestDTO;
import com.stockservice.entities.Product;
import com.stockservice.entities.StockMovement;
import com.stockservice.entities.enums.EStatus;
import com.stockservice.exception.ErrorType;
import com.stockservice.exception.StockServiceException;
import com.stockservice.repositories.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementService
{
    private final StockMovementRepository stockMovementRepository;
    private final ProductService productService;
    private final WareHouseService wareHouseService;

    public Boolean save(StockMovementSaveDTO dto)
    {
        Product product = productService.findById(dto.productId());
        wareHouseService.findById(dto.warehouseId());
        if (product.getStockCount() < dto.quantity())
        {
            throw new StockServiceException(ErrorType.INSUFFICIENT_STOCK);
        }
        stockMovementRepository.save(StockMovement
                .builder()
                .productId(dto.productId())
                .warehouseId(dto.warehouseId())
                .quantity(dto.quantity())
                .stockMovementType(dto.stockMovementType())
                .build());
        return null;
    }

    public Boolean delete(Long id)
    {
        StockMovement stockMovement = stockMovementRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND));
        stockMovement.setStatus(EStatus.DELETED);
        stockMovementRepository.save(stockMovement);
        return true;
    }

    public Boolean update(StockMovementUpdateRequestDTO dto)
    {
        Product product = productService.findById(dto.productId());
        wareHouseService.findById(dto.warehouseId());
        if (product.getStockCount() < dto.quantity())
        {
            throw new StockServiceException(ErrorType.INSUFFICIENT_STOCK);
        }
        StockMovement stockMovement = stockMovementRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND));

        stockMovement.setProductId(dto.productId());
        stockMovement.setWarehouseId(dto.warehouseId());

        if (dto.quantity() != null)
        {
            stockMovement.setQuantity(dto.quantity());
        }
        if (dto.stockMovementType() != null)
        {
            stockMovement.setStockMovementType(dto.stockMovementType());
        }

        stockMovementRepository.save(stockMovement);

            return true;
        }

    public List<StockMovement> findAll(PageRequestDTO dto)
    {

        return stockMovementRepository.findAll(PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public StockMovement findById(Long id)
    {
        return stockMovementRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND));
    }
}

