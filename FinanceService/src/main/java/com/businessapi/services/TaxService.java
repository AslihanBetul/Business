package com.businessapi.services;

import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.request.TaxSaveRequestDTO;
import com.businessapi.dto.request.TaxUpdateRequestDTO;
import com.businessapi.entity.Tax;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.FinanceServiceException;
import com.businessapi.repositories.TaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepository taxRepository;

    public Boolean save(TaxSaveRequestDTO dto) {
        Tax tax = Tax.builder()
                .taxType(dto.taxType())
                .taxRate(dto.taxRate())
                .description(dto.description())
                .build();

        taxRepository.save(tax);
        return true;
    }

    public Boolean update(TaxUpdateRequestDTO dto) {
        Tax tax = taxRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.TAX_NOT_FOUND));
        tax.setTaxType(dto.taxType());
        tax.setTaxRate(dto.taxRate());
        tax.setDescription(dto.description());

        taxRepository.save(tax);
        return true;
    }

    public Boolean delete(Long id) {
        Tax tax = taxRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.TAX_NOT_FOUND));
        tax.setStatus(EStatus.DELETED);
        taxRepository.save(tax);
        return true;
    }

    public List<Tax> findAll(PageRequestDTO dto) {
        return taxRepository.findAll(PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public Tax findById(Long id) {
        return taxRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.TAX_NOT_FOUND));
    }
}
