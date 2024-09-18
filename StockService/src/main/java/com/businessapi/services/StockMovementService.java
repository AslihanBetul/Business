package com.businessapi.services;

import com.businessapi.RabbitMQ.Model.CustomerNameLastNameResponseModel;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.request.StockMovementSaveDTO;
import com.businessapi.dto.request.StockMovementUpdateRequestDTO;
import com.businessapi.dto.response.SellOrderResponseDTO;
import com.businessapi.dto.response.StockMovementResponseDTO;
import com.businessapi.entities.Order;
import com.businessapi.entities.Product;
import com.businessapi.entities.StockMovement;
import com.businessapi.entities.enums.EOrderType;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import com.businessapi.repositories.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public StockMovement findById(Long id)
    {
        return stockMovementRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND));
    }

    /**
     * Finds products with name containing search text
     * Finds sell orders with respect to pagination
     * Converts orders to StockMovementResponseDTO
     * @param dto search text , page number , page size parameters
     * @return List of StockMovementResponseDTO
     */
    public List<StockMovementResponseDTO> findAll(PageRequestDTO dto)
    {
        //Finds products with name containing search text
        List<Product> products = productService.findAllByProductNameContainingIgnoreCase(dto.searchText());
        //Mapping products to their ids
        List<Long> productIdList = products.stream().map(Product::getId).collect(Collectors.toList());
        //Finds buy orders with respect to pagination, order type and product ids
        List<StockMovement> stockList = stockMovementRepository.findAllByProductIdIn(productIdList, PageRequest.of(dto.page(), dto.size()));
        List<StockMovementResponseDTO> stockMovementDtoList = new ArrayList<>();
        //Converting orders to BuyOrderResponseDTO and finding productName + supplierName
        stockList.stream().forEach(stock ->
        {
            String productName = products.stream().filter(product -> product.getId() == stock.getProductId()).findFirst().get().getName();
            String wareHouseName = wareHouseService.findById(stock.getWarehouseId()).getName();
            stockMovementDtoList.add(new StockMovementResponseDTO(stock.getId(), productName,wareHouseName,stock.getQuantity(),stock.getStatus(),stock.getStockMovementType(),stock.getCreatedAt()));
        });
        return stockMovementDtoList.stream()
                .sorted(Comparator.comparing(StockMovementResponseDTO::productName))
                .collect(Collectors.toList());
    }
}

