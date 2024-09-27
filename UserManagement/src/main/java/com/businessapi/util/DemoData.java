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
        roles.add(roleRepository.findById(2L).get());
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
        Role member = Role.builder().roleName("MEMBER").build();

        Role adminRole = Role.builder()
                .roleName("ADMIN")
                .roleDescription("VP")
                .build();

        Role pym = Role.builder().roleName("PYM").roleDescription("Proje Yönetimi Modülü Satın Alan Kullanıcı").build();
        Role crm = Role.builder().roleName("CRM").roleDescription("CRM Modülü Satın Alan Kullanıcı").build();
        Role imm = Role.builder().roleName("IMM").roleDescription("Envanter Yönetimi Modülü Satın Alan Kullanıcı").build();
        Role hrmm = Role.builder().roleName("HRMM").roleDescription("İK Yönetimi Modülü Satın Alan Kullanıcı").build();
        Role fam = Role.builder().roleName("FAM").roleDescription("Finans ve Muhasebe Modülü Satın Alan Kullanıcı").build();
        Role oam = Role.builder().roleName("OAM").roleDescription("Analiz Modülü Satın Alan Kullanıcı").build();


        roleRepository.save(superAdminRole); //1
        roleRepository.save(adminRole); //2
        roleRepository.save(member);    //3
        roleRepository.save(pym);   //4
        roleRepository.save(crm);   //5
        roleRepository.save(imm);   //6
        roleRepository.save(hrmm);  //7
        roleRepository.save(fam);   //8
        roleRepository.save(oam);   //9
    }


    private void saveUsers(){
        List<Long> roles = new ArrayList<>(List.of(3L, 4L, 5L, 6L, 7L, 8L, 9L));
        UserSaveRequestDTO user = new UserSaveRequestDTO("Super Member","USER","member@example.com","123",roles);
        userService.saveUser(user);
    }

}
