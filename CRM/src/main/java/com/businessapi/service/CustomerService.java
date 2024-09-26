package com.businessapi.service;

import com.businessapi.RabbitMQ.Model.CustomerNameLastNameResponseModel;
import com.businessapi.RabbitMQ.Model.CustomerResponseWithIdModel;
import com.businessapi.RabbitMQ.Model.CustomerReturnId;
import com.businessapi.RabbitMQ.Model.EmailResponseModel;
import com.businessapi.dto.request.*;
import com.businessapi.dto.response.CustomerResponseDTO;
import com.businessapi.entity.Customer;

import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.mapper.CustomerMapper;

import com.businessapi.repository.CustomerRepository;
import com.businessapi.utility.JwtTokenManager;
import com.businessapi.utility.SessionManager;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final JwtTokenManager jwtTokenManager;
    private final RabbitTemplate rabbitTemplate;


    public Boolean save(CustomerSaveDTO dto) {
        if (customerRepository.findCustomerByEmailIgnoreCase(dto.email()).isPresent()) {
            throw new CustomerServiceException(ErrorType.EMAIL_ALREADY_EXISTS);
        }
        Customer customer = Customer.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .phone(dto.phone())
                .address(dto.address())
                .memberId(SessionManager.memberId)
                .build();
        customer.setStatus(EStatus.ACTIVE);
        customerRepository.save(customer);
        return true;
    }

    // This method will return members customers with paginable
    public List<Customer> findAll(PageRequestDTO dto) {
        return customerRepository.findAllByFirstNameContainingIgnoreCaseAndStatusIsNotAndMemberIdOrderByFirstNameAsc(dto.searchText(), EStatus.DELETED,SessionManager.memberId, PageRequest.of(dto.page(), dto.size()));

    }

    // This method will update customer by token //TEST
    public Boolean update(CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = customerRepository.findById(customerUpdateDTO.id()).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
        if (customer.getStatus() != EStatus.DELETED && customer.getStatus() != EStatus.PENDING) {
            customer.setFirstName(customerUpdateDTO.firstName() != null ? customerUpdateDTO.firstName() : customer.getFirstName());
            customer.setLastName(customerUpdateDTO.lastName() != null ? customerUpdateDTO.lastName() : customer.getLastName());
            customer.setPhone(customerUpdateDTO.phone() != null ? customerUpdateDTO.phone() : customer.getPhone());
            customer.setAddress(customerUpdateDTO.address() != null ? customerUpdateDTO.address() : customer.getAddress());
            customer.setEmail(customerUpdateDTO.email() != null ? customerUpdateDTO.email() : customer.getEmail());
            customerRepository.save(customer);
            return true;
        } else {
            throw new CustomerServiceException(ErrorType.CUSTOMER_NOT_ACTIVE);
        }
    }

    // This method will delete customer by token
    public Boolean delete(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
        if (customer.getStatus() == EStatus.DELETED) {
            throw new CustomerServiceException(ErrorType.CUSTOMER_ALREADY_DELETED);
        }
        customer.setStatus(EStatus.DELETED);
        return true;
    }






}
