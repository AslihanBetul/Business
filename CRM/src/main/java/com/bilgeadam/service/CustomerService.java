package com.bilgeadam.service;

import com.bilgeadam.dto.request.CustomerSaveDTO;
import com.bilgeadam.entity.Customer;
import com.bilgeadam.exception.CustomerServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.CustomerMapper;
import com.bilgeadam.repository.CustomerRepository;
import com.bilgeadam.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
   // private final JwtTokenManager jwtTokenManager;

    // a record from the queue will come here
    public Boolean save(CustomerSaveDTO customerSaveDTO) {

        if (customerRepository.existsByEmail(customerSaveDTO.email())) {
            throw new CustomerServiceException(ErrorType.CUSTOMER_EMAIL_EXIST);
        }
        customerRepository.save(CustomerMapper.INSTANCE.customerSaveDTOToCustomer(customerSaveDTO));
        return true;
    }

    public Customer findByAuthId(Long authid) {
        return customerRepository.findByAuthId(authid).orElseThrow(()-> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
    }
}
