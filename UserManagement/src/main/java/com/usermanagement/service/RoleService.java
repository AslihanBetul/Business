package com.usermanagement.service;

import com.usermanagement.dto.requestDTOs.RoleCreateDTO;
import com.usermanagement.dto.requestDTOs.RoleUpdateRequestDTO;
import com.usermanagement.dto.responseDTOs.RoleResponseDTO;
import com.usermanagement.entity.Role;
import com.usermanagement.entity.User;
import com.usermanagement.entity.enums.EStatus;
import com.usermanagement.exception.ErrorType;
import com.usermanagement.exception.UserException;
import com.usermanagement.mapper.RoleMapper;
import com.usermanagement.repository.RoleRepository;
import com.usermanagement.views.GetAllRoleView;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public void createUserRole(RoleCreateDTO roleCreateDTO) {
        Role role = RoleMapper.INSTANCE.roleCreateDTOToRole(roleCreateDTO);
        role.setRoleName(role.getRoleName().toUpperCase());
        roleRepository.save(role);
    }

    public List<Role> getRolesByRoleId(List<Long> roleIds) {
        List<Role> findedRoles = roleRepository.findAllById(roleIds);
        if (findedRoles.isEmpty()) {
            throw new UserException(ErrorType.ROLE_DATA_IS_EMPTY);
        }
        return findedRoles;
    }

    public void updateUserRole(RoleUpdateRequestDTO roleUpdateRequestDTO) {
        Role role = roleRepository.findById(roleUpdateRequestDTO.roleId()).orElseThrow(() -> new UserException(ErrorType.ROLE_NOT_FOUND));
        role.setRoleName(roleUpdateRequestDTO.roleName());
        role.setRoleDescription(roleUpdateRequestDTO.roleDescription());
        roleRepository.save(role);
    }

    public void deleteUserRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new UserException(ErrorType.ROLE_NOT_FOUND));
        role.setStatus(EStatus.DELETED);
        roleRepository.save(role);
    }

    public List<RoleResponseDTO> getAllUserRoles() {
        List<GetAllRoleView> allroles = roleRepository.getAllRoles(EStatus.ACTIVE);

        List<RoleResponseDTO> roleResponseDTOs = new ArrayList<>();

        allroles.forEach(role -> {
            roleResponseDTOs.add(RoleMapper.INSTANCE.getAllRoleViewToRoleResponseDTO(role));
        });

        return roleResponseDTOs;

    }

    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new UserException(ErrorType.ROLE_NOT_FOUND));
    }


    public List<RoleResponseDTO> getAllAssignableRoles(User user) {
        List<Role> allRoles = roleRepository.findAll();
        allRoles.removeAll(user.getRole());
        List<RoleResponseDTO> roleResponseDTOs = new ArrayList<>();
        allRoles.forEach(role -> {
            roleResponseDTOs.add(RoleMapper.INSTANCE.roleToRoleResponseDTO(role));
        });

        return roleResponseDTOs;
    }




}
