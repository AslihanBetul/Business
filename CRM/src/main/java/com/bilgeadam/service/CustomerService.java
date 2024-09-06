package com.bilgeadam.service;

import com.bilgeadam.dto.request.CustomerSaveDTO;
import com.bilgeadam.dto.request.CustomerUpdateDTO;
import com.bilgeadam.dto.response.CustomerResponseDTO;
import com.bilgeadam.entity.Customer;
import com.bilgeadam.exception.CustomerServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.CustomerMapper;
import com.bilgeadam.repository.CustomerRepository;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final JwtTokenManager jwtTokenManager;

    // a record from the queue will come here
    public Boolean save(CustomerSaveDTO customerSaveDTO) {

        if (customerRepository.existsByEmail(customerSaveDTO.email())) {
            throw new CustomerServiceException(ErrorType.CUSTOMER_EMAIL_EXIST);
        }
        Customer customer = customerRepository.save(CustomerMapper.INSTANCE.customerSaveDTOToCustomer(customerSaveDTO));
        customer.setStatus(EStatus.ACTIVE);
        customerRepository.save(customer);
        return true;
    }

    // This use for security methods
    public Customer findByAuthId(Long authid) {
        return customerRepository.findByAuthId(authid).orElseThrow(()-> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
    }

    // This method will return all customers
    public List<CustomerResponseDTO> findAll() {
        return CustomerMapper.INSTANCE.customersToCustomerResponseDTOs(customerRepository.findAll());
    }

    // This method will find customer by id
    public CustomerResponseDTO findById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
        return CustomerMapper.INSTANCE.customerToCustomerResponseDTO(customer);
    }


    // This method will update customer by token
    public Boolean update(CustomerUpdateDTO customerUpdateDTO) {
        Long authId = getAuthIdFromToken(customerUpdateDTO.token());

        Customer customer = customerRepository.findByAuthId(authId).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
        if (customer.getStatus() != EStatus.DELETED && customer.getStatus() != EStatus.PENDING) {
            customer.setFirstName(customerUpdateDTO.firstName()!=null?customerUpdateDTO.firstName():customer.getFirstName());
            customer.setLastName(customerUpdateDTO.lastName()!=null?customerUpdateDTO.lastName():customer.getLastName());
            customer.setPhone(customerUpdateDTO.phone()!=null?customerUpdateDTO.phone():customer.getPhone());
            customer.setAddress(customerUpdateDTO.address()!=null?customerUpdateDTO.address():customer.getAddress());
            customer.setEmail(customerUpdateDTO.email()!=null?customerUpdateDTO.email():customer.getEmail());
            customerRepository.save(customer);
            return true;
        }
        else {
            throw new CustomerServiceException(ErrorType.CUSTOMER_NOT_ACTIVE);
        }



    }

    private Long getAuthIdFromToken(String token) {
        return jwtTokenManager.getIdFromToken(token).orElseThrow(()-> new CustomerServiceException(ErrorType.INVALID_TOKEN));

    }

    // This method will delete customer by token
    public Boolean delete(String token) {
        Long authId = getAuthIdFromToken(token);
        Customer customer = customerRepository.findByAuthId(authId).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
        customer.setStatus(EStatus.DELETED);
        return true;
    }
}
