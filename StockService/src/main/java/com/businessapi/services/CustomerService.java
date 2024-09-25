package com.businessapi.services;

import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.response.CustomerSaveRequestDTO;
import com.businessapi.dto.response.CustomerUpdateRequestDTO;
import com.businessapi.entities.Customer;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import com.businessapi.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService
{
    private final CustomerRepository customerRepository;

    public Boolean save(CustomerSaveRequestDTO dto)
    {
        if (customerRepository.findCustomerByEmailIgnoreCase(dto.email()).isPresent()) {
            throw new StockServiceException(ErrorType.EMAIL_ALREADY_EXISTS);
        }
        customerRepository.save(Customer.builder().name(dto.name()).surname(dto.surname()).email(dto.email()).build());
        return true;
    }

    public Boolean delete(Long id)
    {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.CUSTOMER_NOT_FOUND));
        customer.setStatus(EStatus.DELETED);
        customerRepository.save(customer);
        return true;
    }

    public Boolean update(CustomerUpdateRequestDTO dto)
    {
        Customer customer = customerRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.CUSTOMER_NOT_FOUND));
        customer.setName(dto.name());
        customer.setSurname(dto.surname());
        customer.setEmail(dto.email());
        customerRepository.save(customer);
        return true;
    }

    public List<Customer> findAll(PageRequestDTO dto)
    {
        return customerRepository.findAllByNameContainingIgnoreCaseAndStatusIsNotOrderByNameAsc(dto.searchText(), EStatus.DELETED, PageRequest.of(dto.page(), dto.size()));
    }

    public Customer findById(Long id)
    {
        return customerRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.CUSTOMER_NOT_FOUND));
    }
}
