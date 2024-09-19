package com.businessapi.repository;

import com.businessapi.entity.Customer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByFirstNameContainingIgnoreCase(String firstName);

    List<Customer> findAllByFirstNameContainingIgnoreCaseOrderByFirstNameAsc(String firstName, PageRequest pageRequest);
    List<Customer> findCustomerByUserIdAndFirstNameContainingIgnoreCaseOrderByFirstNameAsc(Long userId, String firstName, PageRequest pageRequest);

    List<Customer> findCustomersByUserId(Long userId);


}
