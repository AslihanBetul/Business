package com.businessapi.repositories;

import com.businessapi.entity.Invoice;
import com.businessapi.entity.enums.EInvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByInvoiceStatusIn(Collection<EInvoiceStatus> invoiceStatus);
}
