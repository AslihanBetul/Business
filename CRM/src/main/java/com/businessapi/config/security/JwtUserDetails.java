package com.businessapi.config.security;


import com.businessapi.dto.response.CustomerResponseDTO;
import com.businessapi.service.CustomerService;
import com.businessapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {

    private final UserService userService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByAuthid(Long authid) {
        Long userId = userService.findByAuthId(authid);
         com.businessapi.entity.User user =userService.findById(userId);
        return User.builder()
                    .username(user.getFirstName() + user.getLastName())
                    .password("")
                    .build();
    }



}
