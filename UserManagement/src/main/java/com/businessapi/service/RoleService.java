package com.businessapi.service;

import com.businessapi.dto.requestDTOs.PageRequestDTO;
import com.businessapi.dto.requestDTOs.RoleCreateDTO;
import com.businessapi.dto.requestDTOs.RoleUpdateRequestDTO;
import com.businessapi.dto.requestDTOs.UpdateUserRoleStatusDTO;
import com.businessapi.dto.responseDTOs.PageableRoleListResponseDTO;
import com.businessapi.dto.responseDTOs.RoleResponseDTO;
import com.businessapi.entity.Role;
import com.businessapi.entity.User;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.UserException;
import com.businessapi.mapper.RoleMapper;
import com.businessapi.repository.RoleRepository;
import com.businessapi.views.GetAllRoleView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public PageableRoleListResponseDTO getAllUserRoles(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size());
        Page<GetAllRoleView> allRoles = roleRepository.getAllRolesWithSearch(pageRequestDTO.searchText(), pageable);

        List<RoleResponseDTO> roleResponseDTOs = new ArrayList<>();
        allRoles.getContent().forEach(role -> {
            roleResponseDTOs.add(RoleMapper.INSTANCE.getAllRoleViewToRoleResponseDTO(role));
        });
        return PageableRoleListResponseDTO.builder().roleList(roleResponseDTOs).currentPage(allRoles.getNumber()).totalPages(allRoles.getTotalPages()).totalElements(allRoles.getTotalElements()).build();

    }

    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new UserException(ErrorType.ROLE_NOT_FOUND));
    }


    public List<RoleResponseDTO> getAllAssignableRoles(User user) {
        List<Role> allRoles = roleRepository.findAll();
        allRoles.removeIf(role -> role.getRoleName().equals("SUPER_ADMIN")|| !role.getStatus().equals(EStatus.ACTIVE));
        allRoles.removeAll(user.getRole());
        List<RoleResponseDTO> roleResponseDTOs = new ArrayList<>();
        allRoles.forEach(role -> {
            roleResponseDTOs.add(RoleMapper.INSTANCE.roleToRoleResponseDTO(role));
        });

        return roleResponseDTOs;
    }


    public Boolean checkIfRoleExistsByRoleName(String roleName) {
        return roleRepository.existsByRoleNameIgnoreCase(roleName);
    }


    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleNameIgnoreCase(roleName).orElseThrow(()-> new UserException(ErrorType.ROLE_NOT_FOUND));
    }

    public Boolean updateUserRoleStatus(UpdateUserRoleStatusDTO updateUserRoleStatusDTO) {
        Role role = roleRepository.findById(updateUserRoleStatusDTO.roleId()).orElseThrow(() -> new UserException(ErrorType.ROLE_NOT_FOUND));
        role.setStatus(updateUserRoleStatusDTO.status());
        roleRepository.save(role);

        return null;
    }
}
