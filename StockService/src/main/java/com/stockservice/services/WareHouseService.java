package com.stockservice.services;

import com.stockservice.dto.request.PageRequestDTO;
import com.stockservice.dto.request.WareHouseSaveRequestDTO;
import com.stockservice.dto.request.WareHouseUpdateRequestDTO;
import com.stockservice.entities.WareHouse;
import com.stockservice.entities.enums.EStatus;
import com.stockservice.exception.ErrorType;
import com.stockservice.exception.StockServiceException;
import com.stockservice.repositories.SupplierRepository;
import com.stockservice.repositories.WareHouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WareHouseService
{
    private final WareHouseRepository wareHouseRepository;

    public Boolean save(WareHouseSaveRequestDTO dto)
    {
        wareHouseRepository.save(WareHouse
                .builder()
                .name(dto.name())
                .location(dto.location())
                .build());
        return true;
    }

    public Boolean delete(Long id)
    {
        WareHouse wareHouse = wareHouseRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.WAREHOUSE_NOT_FOUND));
        wareHouse.setStatus(EStatus.DELETED);
        wareHouseRepository.save(wareHouse);
        return true;
    }

    public Boolean update(WareHouseUpdateRequestDTO dto)
    {
        WareHouse wareHouse = wareHouseRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.WAREHOUSE_NOT_FOUND));
        if (dto.location() != null)
        {
            wareHouse.setLocation(dto.location());
        }
        if (dto.name() != null)
        {
            wareHouse.setName(dto.name());
        }
        wareHouseRepository.save(wareHouse);
        return true;
    }


    public List<WareHouse> findAll(PageRequestDTO dto)
    {
       return wareHouseRepository.findAllByNameContaining(dto.searchText(), PageRequest.of(dto.page(), dto.size()));
    }

    public WareHouse findById(Long id)
    {
        return wareHouseRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.WAREHOUSE_NOT_FOUND));
    }
}
