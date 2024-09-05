package com.usermanagement.service;

import com.usermanagement.dto.requestDTOs.UserDeleteRequestDTO;
import com.usermanagement.dto.requestDTOs.UserSaveRequestDTO;
import com.usermanagement.dto.requestDTOs.UserUpdateRequestDTO;
import com.usermanagement.entity.Role;
import com.usermanagement.entity.User;
import com.usermanagement.entity.enums.EStatus;
import com.usermanagement.exception.ErrorType;
import com.usermanagement.exception.UserException;
import com.usermanagement.mapper.UserMapper;
import com.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public void saveUser(UserSaveRequestDTO userSaveRequestDTO) {
        User user = UserMapper.INSTANCE.userSaveRequestDTOToUser(userSaveRequestDTO);
        List<Role> usersRoles = roleService.getRolesByRoleId(userSaveRequestDTO.getRoleIds());
        user.setRole(usersRoles);
        userRepository.save(user);
    }

    public void deleteUser(UserDeleteRequestDTO userDeleteRequestDTO) {
        User user = userRepository.findById(userDeleteRequestDTO.getUserId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setStatus(EStatus.DELETED);
        userRepository.save(user);
    }

    public void updateUser(UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository.findByAuthId(userUpdateRequestDTO.getAuthId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setFirstName(userUpdateRequestDTO.getFirstName());
        user.setLastName(userUpdateRequestDTO.getLastName());
        //TODO mail adresi burada auth'a göderilecek.
        userRepository.save(user);
    }
}
