package com.businessapi.services;

import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.request.SupplierSaveRequestDTO;
import com.businessapi.dto.request.SupplierUpdateRequestDTO;
import com.businessapi.entities.Order;
import com.businessapi.entities.Product;
import com.businessapi.entities.Supplier;
import com.businessapi.entities.enums.EOrderType;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import com.businessapi.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService
{
    private final SupplierRepository supplierRepository;
    private final OrderService orderService;
    private final ProductService productService;

    public Boolean save(SupplierSaveRequestDTO dto)
    {

        supplierRepository.save(Supplier
                .builder()
                .name(dto.name())
                .contactInfo(dto.contactInfo())
                .address(dto.address())
                .notes(dto.notes())
                .build());

        return true;
    }

    public Boolean delete(Long id)
    {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.SUPPLIER_NOT_FOUND));
        supplier.setStatus(EStatus.DELETED);
        supplierRepository.save(supplier);
        return true;
    }

    public Boolean update(SupplierUpdateRequestDTO dto)
    {
        Supplier supplier = supplierRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.SUPPLIER_NOT_FOUND));
        if (dto.name() != null)
        {
            supplier.setName(dto.name());
        }
        if (dto.contactInfo() != null)
        {
            supplier.setContactInfo(dto.contactInfo());
        }
        if (dto.address() != null)
        {
            supplier.setAddress(dto.address());
        }
        if (dto.notes() != null)
        {
            supplier.setNotes(dto.notes());
        }
        supplierRepository.save(supplier);
        return true;
    }

    public List<Supplier> findAll(PageRequestDTO dto)
    {
        return supplierRepository.findAllByNameContainingIgnoreCase(dto.searchText(), PageRequest.of(dto.page(), dto.size()));
    }

    public Supplier findById(Long id)
    {
        return supplierRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.SUPPLIER_NOT_FOUND));
    }

    public Boolean approveOrder(Long id)
    {
        Order order = orderService.findById(id);
        if (order.getStatus() != EStatus.ACTIVE)
        {
            throw new StockServiceException(ErrorType.ORDER_NOT_ACTIVE);
        }
        if (order.getOrderType() != EOrderType.BUY)
        {
            throw new StockServiceException(ErrorType.WRONG_ORDER_TYPE);
        }
        //TODO THERE SHOULD BE A CHECK FOR IF SUPPLIER ID = REQUEST'S SUPPLIER ID
        //TODO MONEY MECHANISM CAN BE IMPLEMENTED LATER
        Product product = productService.findById(order.getProductId());
        order.setStatus(EStatus.APPROVED);
        product.setIsProductAutoOrdered(false);
        product.setStockCount(product.getStockCount()+order.getQuantity());
        orderService.save(order);
        return true;
    }
}
