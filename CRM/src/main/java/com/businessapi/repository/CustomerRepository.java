package com.businessapi.repository;

import com.businessapi.entity.Customer;
import com.businessapi.utility.enums.EStatus;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByFirstNameContainingIgnoreCase(String firstName);

    List<Customer> findAllByFirstNameContainingIgnoreCaseAndStatusIsNotAndMemberIdOrderByFirstNameAsc(String s, EStatus status,Long memberId, PageRequest of);

    Optional<Customer> findCustomerByEmailIgnoreCase(String email);
}
