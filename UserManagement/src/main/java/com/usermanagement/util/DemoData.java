package com.usermanagement.util;

import com.usermanagement.entity.Role;
import com.usermanagement.entity.User;
import com.usermanagement.entity.enums.EStatus;
import com.usermanagement.repository.RoleRepository;
import com.usermanagement.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DemoData {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    //@PostConstruct
    public void saveDate(){
        saveBaseRoles();
        saveSuperAdmin();
    }

    private void saveSuperAdmin(){
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(1L).get());
        User superAdmin = User.builder()
                .role(roles)
                .authId(1L)
                .firstName("Super")
                .lastName("Admin")
                .status(EStatus.ACTIVE)
                .build();
        userRepository.save(superAdmin);
    }

    private void saveBaseRoles(){
        Role superAdminRole = Role.builder()
                .roleName("SUPER_ADMIN")
                .roleDescription("God of The App")
                .build();
        roleRepository.save(superAdminRole);
    }




}
