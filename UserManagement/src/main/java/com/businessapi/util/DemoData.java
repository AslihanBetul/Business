package com.businessapi.util;

import com.businessapi.dto.requestDTOs.UserSaveRequestDTO;
import com.businessapi.entity.Role;
import com.businessapi.entity.User;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.repository.RoleRepository;
import com.businessapi.repository.UserRepository;

import com.businessapi.service.UserService;
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
    private final UserService userService;


    @PostConstruct
    public void saveDate(){
        saveBaseRoles();
        saveSuperAdmin();
        saveUsers();
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

        Role unassignedRole = Role.builder()
                .roleName("UNASSIGNED")
                .roleDescription("Role that user who pending role from super admin or admin. They can update their user profile only!!").build();

        Role customer = Role.builder().roleName("CUSTOMER").build();

        Role adminRole = Role.builder()
                .roleName("ADMIN")
                .roleDescription("VP")
                .build();

        roleRepository.save(superAdminRole);
        roleRepository.save(adminRole);
        roleRepository.save(unassignedRole);
        roleRepository.save(customer);
    }


    private void saveUsers(){
        List<Long> roles = new ArrayList<>();
        roles.add(4L);
        roles.add(3L);
        UserSaveRequestDTO user = new UserSaveRequestDTO("Salih","ER","ertugrulsaliher@gmail.com","123",roles);
        userService.saveUser(user);
    }

}
