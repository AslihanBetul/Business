package com.stockservice.services;

import com.stockservice.dto.request.PageRequestDTO;
import com.stockservice.dto.request.SupplierSaveRequestDTO;
import com.stockservice.dto.request.SupplierUpdateRequestDTO;
import com.stockservice.entities.StockMovement;
import com.stockservice.entities.Supplier;
import com.stockservice.entities.enums.EStatus;
import com.stockservice.exception.ErrorType;
import com.stockservice.exception.StockServiceException;
import com.stockservice.repositories.StockMovementRepository;
import com.stockservice.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService
{
    private final SupplierRepository supplierRepository;

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
        return supplierRepository.findAllByNameContaining(dto.searchText(), PageRequest.of(dto.page(), dto.size()));
    }

    public Supplier findById(Long id)
    {
        return supplierRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.SUPPLIER_NOT_FOUND));
    }
}
