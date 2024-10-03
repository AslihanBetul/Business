package com.businessapi.repositories;

import com.businessapi.entity.Invoice;
import com.businessapi.entity.enums.EInvoiceStatus;
import com.businessapi.entity.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findAllByStatusNot(EStatus status, Pageable pageable);

}
