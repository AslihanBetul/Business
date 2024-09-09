package com.usermanagement.service;

import com.usermanagement.RabbitMQ.Model.AuthMailUpdateFromUser;
import com.usermanagement.RabbitMQ.Model.CustomerSaveFromUserModel;
import com.usermanagement.RabbitMQ.Model.SaveUserFromAuthModel;
import com.usermanagement.RabbitMQ.Model.UserRoleListModel;
import com.usermanagement.dto.requestDTOs.AddRoleToUserRequestDTO;
import com.usermanagement.dto.requestDTOs.UserDeleteRequestDTO;
import com.usermanagement.dto.requestDTOs.UserSaveRequestDTO;
import com.usermanagement.dto.requestDTOs.UserUpdateRequestDTO;
import com.usermanagement.dto.responseDTOs.GetAllUsersResponseDTO;
import com.usermanagement.entity.Role;
import com.usermanagement.entity.User;
import com.usermanagement.entity.enums.EStatus;
import com.usermanagement.exception.ErrorType;
import com.usermanagement.exception.UserException;
import com.usermanagement.mapper.RoleMapper;
import com.usermanagement.mapper.UserMapper;
import com.usermanagement.repository.UserRepository;
import com.usermanagement.views.GetAllUsersView;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RabbitTemplate rabbitTemplate;

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new UserException(ErrorType.USER_NOT_FOUND));
    }

    public void saveUser(UserSaveRequestDTO userSaveRequestDTO) {
        User user = UserMapper.INSTANCE.userSaveRequestDTOToUser(userSaveRequestDTO);
        List<Role> usersRoles = roleService.getRolesByRoleId(userSaveRequestDTO.roleIds());
        user.setRole(usersRoles);
        user.setStatus(EStatus.ACTIVE);
        userRepository.save(user);
    }

    public void deleteUser(UserDeleteRequestDTO userDeleteRequestDTO) {
        User user = userRepository.findById(userDeleteRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setStatus(EStatus.DELETED);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository.findByAuthId(userUpdateRequestDTO.authId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setFirstName(userUpdateRequestDTO.firstName());
        user.setLastName(userUpdateRequestDTO.lastName());
        sendUserMailToAuthService(AuthMailUpdateFromUser.builder().authId(user.getAuthId()).email(userUpdateRequestDTO.email()).build());
        userRepository.save(user);
    }

    private void sendUserMailToAuthService(AuthMailUpdateFromUser authMailUpdateFromUser) {
        rabbitTemplate.convertAndSend("businessDirectExchange","keyAuthMailUpdateFromUser", authMailUpdateFromUser);
    }


    @Transactional
    public void saveUserFromAuthService(SaveUserFromAuthModel saveUserFromAuthModel){
        List<Role> usersRoles = new ArrayList<>();
        usersRoles.add(roleService.getRoleById(2L));
        User user = User.builder()
                .authId(saveUserFromAuthModel.getAuthId())
                .firstName(saveUserFromAuthModel.getFirstName())
                .lastName(saveUserFromAuthModel.getLastName())
                .status(EStatus.PENDING)
                .role(usersRoles)
                .build();
        userRepository.save(user);
    }

    //Farklı bir servise taşınabilir
    @RabbitListener(queues = "queueSaveUserFromAuth")
    private void listenAndSaveUserFromAuthService(SaveUserFromAuthModel saveUserFromAuthModel){
        saveUserFromAuthService(saveUserFromAuthModel);
    }

    public List<GetAllUsersResponseDTO> getAllUser() {
        List<User> allUsersList = userRepository.findAll();

        List<GetAllUsersResponseDTO> allUsersResponseDTOList = new ArrayList<>();

       allUsersList.forEach(user -> {
           allUsersResponseDTOList.add(GetAllUsersResponseDTO.builder()
                   .userId(user.getId())
                   .firstName(user.getFirstName())
                   .lastName(user.getLastName())
                   .userRoles(RoleMapper.INSTANCE.rolesToRoleResponseDTOList(user.getRole()))
                   .build());
       });


        return allUsersResponseDTOList;
    }

    public void addRoleToUser(AddRoleToUserRequestDTO addRoleToUserRequestDTO) {
        User user = userRepository.findById(addRoleToUserRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        Role roleById = roleService.getRoleById(addRoleToUserRequestDTO.roleId());
        user.getRole().add(roleById);
        isUserCustomer(user);
        userRepository.save(user);

    }

    private void isUserCustomer(User user) {
        user.getRole().forEach(role -> {
            if(role.getRoleName().equals("CUSTOMER")){
                sendUserInfoForSaveCustomer(CustomerSaveFromUserModel.builder().authId(user.getAuthId()).userId(user.getId()).status(user.getStatus()).build());
            }
        });
    }

    private void sendUserInfoForSaveCustomer(CustomerSaveFromUserModel customerSaveFromUserModel){
        rabbitTemplate.convertAndSend("businessDirectExchange","keySaveCustomerFromUser",customerSaveFromUserModel);
    }

    @RabbitListener(queues = "queueRolesByAuthId")
    private UserRoleListModel sendAuthRoles(Long authId) {
        return getRolesForSecurity(authId);
    }

    public List<String> getRolesRabbit(Long authId){
        UserRoleListModel userRoleListModel = (UserRoleListModel) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyRolesByAuthId", authId);
        return userRoleListModel.getUserRoles();
    }

    public UserRoleListModel getRolesForSecurity(Long authId){
        List<Role> userRoles = userRepository.getUserRoles(authId);
        List<String> userRolesString = new ArrayList<>();
        userRoles.forEach(role -> {
            userRolesString.add(role.getRoleName());


        });

        return UserRoleListModel.builder().userRoles(userRolesString).build();
    }

}
