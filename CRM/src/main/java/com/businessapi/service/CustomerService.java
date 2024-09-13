package com.businessapi.service;

import com.businessapi.RabbitMQ.Model.CustomerNameLastNameResponseModel;
import com.businessapi.dto.request.CustomerSaveDTO;
import com.businessapi.dto.request.CustomerUpdateDTO;
import com.businessapi.dto.response.CustomerResponseDTO;
import com.businessapi.entity.Customer;

import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.mapper.CustomerMapper;

import com.businessapi.repository.CustomerRepository;
import com.businessapi.utility.JwtTokenManager;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final JwtTokenManager jwtTokenManager;

    @Lazy
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    // a record from the queue will come here //TEST
    public Boolean save(CustomerSaveDTO customerSaveDTO) {

        Customer customer = customerRepository.save(CustomerMapper.INSTANCE.customerSaveDTOToCustomer(customerSaveDTO));
        customer.setStatus(EStatus.ACTIVE);
        customerRepository.save(customer);
        return true;
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



    // This method will update customer by token //TEST
    public Boolean update(CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = findCustomerByToken(customerUpdateDTO.token());
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

    private Customer findCustomerByToken(String token) {
        Long authId = getAuthIdFromToken(token);
        Long customerId = userService.findByAuthIdForCustomer(authId);
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));

    }


    // This method will delete customer by token
    public Boolean delete(String token) {
        Customer customer = findCustomerByToken(token);
        customer.setStatus(EStatus.DELETED);
        return true;
    }

    private Long getAuthIdFromToken(String token) {
        return jwtTokenManager.getIdFromToken(token).orElseThrow(()-> new CustomerServiceException(ErrorType.INVALID_TOKEN));

    }

    public CustomerNameLastNameResponseModel findNameAndLastNameById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
        CustomerNameLastNameResponseModel model = CustomerNameLastNameResponseModel.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
        rabbitTemplate.convertAndSend("businessDirectExchange","keyResponseStock", model);
        return model;
    }
}
