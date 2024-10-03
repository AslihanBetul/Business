package com.businessapi.services;

import com.businessapi.RabbitMQ.Model.InvoiceModel;
import com.businessapi.dto.request.InvoiceSaveRequestDTO;
import com.businessapi.dto.request.InvoiceUpdateRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.entity.Invoice;
import com.businessapi.entity.enums.EInvoiceStatus;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.FinanceServiceException;
import com.businessapi.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public Boolean save(InvoiceSaveRequestDTO dto) {
        Invoice invoice = Invoice.builder()
                .buyerTcNo(dto.buyerTcNo())
                .buyerEmail(dto.buyerEmail())
                .buyerPhone(dto.buyerPhone())
                .productId(dto.productId())
                .quantity(dto.quantity())
                .invoiceDate(dto.invoiceDate())
                .totalAmount(dto.totalAmount())
                .build();

        invoiceRepository.save(invoice);
        return true;
    }

    public Boolean update(InvoiceUpdateRequestDTO dto) {
        Invoice invoice = invoiceRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.INVOICE_NOT_FOUND));
        invoice.setBuyerTcNo(dto.buyerTcNo());
        invoice.setBuyerEmail(dto.buyerEmail());
        invoice.setBuyerPhone(dto.buyerPhone());
        invoice.setProductId(dto.productId());
        invoice.setQuantity(dto.quantity());
        invoice.setInvoiceDate(dto.invoiceDate());
        invoice.setTotalAmount(dto.totalAmount());

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
        return invoiceRepository.findAllByStatusNot(EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.INVOICE_NOT_FOUND));
    }


    @RabbitListener(queues = "queueGetModelFromStockService")
    public void createInvoiceFromModel (InvoiceModel model) {
        Invoice invoice = Invoice.builder()
                .buyerTcNo(model.getBuyerTcNo())
                .buyerEmail(model.getBuyerEmail())
                .buyerPhone(model.getBuyerPhone())
                .productId(model.getProductId())
                .quantity(model.getQuantity())
                .invoiceDate(model.getInvoiceDate())
                .totalAmount(model.getTotalAmount())
                .build();
        invoiceRepository.save(invoice);
    }
}

