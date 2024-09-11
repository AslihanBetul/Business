package com.financeservice.services;

import com.financeservice.dto.request.InvoiceSaveRequestDTO;
import com.financeservice.dto.request.InvoiceUpdateRequestDTO;
import com.financeservice.dto.request.PageRequestDTO;
import com.financeservice.entity.Invoice;
import com.financeservice.entity.enums.EStatus;
import com.financeservice.exception.ErrorType;
import com.financeservice.exception.FinanceServiceException;
import com.financeservice.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public Boolean save(InvoiceSaveRequestDTO dto) {
        Invoice invoice = Invoice.builder()
                .customerIdOrSupplierId(dto.customerIdOrSupplierId())
                .invoiceDate(dto.invoiceDate())
                .totalAmount(dto.totalAmount())
                .paidAmount(dto.paidAmount())
                .invoiceStatus(dto.invoiceStatus())
                .description(dto.description())
                .build();

        invoiceRepository.save(invoice);
        return true;
    }

    public Boolean update(InvoiceUpdateRequestDTO dto) {
        Invoice invoice = invoiceRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.INVOICE_NOT_FOUND));
        invoice.setCustomerIdOrSupplierId(dto.customerIdOrSupplierId());
        invoice.setInvoiceDate(dto.invoiceDate());
        invoice.setTotalAmount(dto.totalAmount());
        invoice.setPaidAmount(dto.paidAmount());
        invoice.setInvoiceStatus(dto.invoiceStatus());
        invoice.setDescription(dto.description());

        invoiceRepository.save(invoice);
        return true;
    }

    public Boolean delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.INVOICE_NOT_FOUND));
        invoice.setStatus(EStatus.DELETED);
        invoiceRepository.save(invoice);
        return true;
    }

    public List<Invoice> findAll(PageRequestDTO dto) {
        return invoiceRepository.findAll(PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.INVOICE_NOT_FOUND));
    }
}

