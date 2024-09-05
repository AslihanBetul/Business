package com.bilgeadam.config.security;

import com.bilgeadam.entity.Customer;
import com.bilgeadam.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {
    private final CustomerService customerService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByAuthid(Long authid) {

        Customer customer = customerService.findByAuthId(authid);

        return User.builder()
                .username(customer.getFirstName()+" "+customer.getLastName())
                .password("")
                .build();
    }

}
