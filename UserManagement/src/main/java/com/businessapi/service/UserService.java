package com.businessapi.service;

import com.businessapi.RabbitMQ.Model.*;
import com.businessapi.dto.requestDTOs.AddRoleToUserRequestDTO;
import com.businessapi.dto.requestDTOs.UserDeleteRequestDTO;
import com.businessapi.dto.requestDTOs.UserSaveRequestDTO;
import com.businessapi.dto.requestDTOs.UserUpdateRequestDTO;
import com.businessapi.dto.responseDTOs.GetAllUsersResponseDTO;
import com.businessapi.entity.Role;
import com.businessapi.entity.User;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.UserException;
import com.businessapi.mapper.RoleMapper;
import com.businessapi.mapper.UserMapper;
import com.businessapi.repository.UserRepository;
import com.businessapi.util.JwtTokenManager;
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
    private final JwtTokenManager jwtTokenManager;

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new UserException(ErrorType.USER_NOT_FOUND));
    }
    public User findByAuthId(Long authId) {
        return userRepository.findByAuthId(authId).orElseThrow(()->new UserException(ErrorType.USER_NOT_FOUND));
    }

    @Transactional
    public void saveUser(UserSaveRequestDTO userSaveRequestDTO) {
        User user = UserMapper.INSTANCE.userSaveRequestDTOToUser(userSaveRequestDTO);
        List<Role> usersRoles = roleService.getRolesByRoleId(userSaveRequestDTO.roleIds());
        user.setRole(usersRoles);
        user.setStatus(EStatus.ACTIVE);

        Long authId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keySaveAuthFromUser", SaveAuthFromUserModel.builder().email(userSaveRequestDTO.email()).password(userSaveRequestDTO.password()).build());
        user.setAuthId(authId);

        userRepository.save(user);
        isUserCustomer(user);
    }



    @RabbitListener(queues = "queueSaveUserFromOtherServices")
    public Long saveUserFromOtherServices(SaveUserFromOtherServicesModel saveUserFromOtherServicesModel) {
        User user = UserMapper.INSTANCE.saveUserFromOtherServicesToUser(saveUserFromOtherServicesModel);
        if(!(roleService.checkIfRoleExistsByRoleName(saveUserFromOtherServicesModel.getRole()))){
            Role role = roleService.saveRole(Role.builder().roleName(saveUserFromOtherServicesModel.getRole().toUpperCase()).build());
            user.getRole().add(role);
        } else{
            Role role = roleService.findByRoleName(saveUserFromOtherServicesModel.getRole());
            user.getRole().add(role);
        }
        user.setStatus(EStatus.ACTIVE);
        Long authId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keySaveAuthFromUser", SaveAuthFromUserModel.builder().email(saveUserFromOtherServicesModel.getEmail()).password(saveUserFromOtherServicesModel.getPassword()).build());
        user.setAuthId(authId);
        userRepository.save(user);
        return authId;
    }





    public void deleteUser(UserDeleteRequestDTO userDeleteRequestDTO) {
        User user = userRepository.findById(userDeleteRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setStatus(EStatus.DELETED);

        rabbitTemplate.convertAndSend("businessDirectExchange","keyDeleteAuth", user.getAuthId());
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
        usersRoles.add(roleService.getRoleById(3L)); //Unassigned rol olarak kaydedilir.
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
                sendUserInfoForSaveCustomer(CustomerSaveFromUserModel.builder()
                        .authId(user.getAuthId())
                        .userId(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .status(user.getStatus()).build());
            }
        });
    }

    private void sendUserInfoForSaveCustomer(CustomerSaveFromUserModel customerSaveFromUserModel){
        rabbitTemplate.convertAndSend("businessDirectExchange","keySaveCustomerFromUser",customerSaveFromUserModel);
    }

    @RabbitListener(queues = "queueRolesByAuthId")
    private UserRoleListModel sendAuthRoles(Long authId) { //private kurallarına bak

        return getRolesForSecurity(authId);
    }



    public UserRoleListModel getRolesForSecurity(Long authId){
        List<Role> userRoles = userRepository.getUserRoles(authId);
        List<String> userRolesString = new ArrayList<>();
        userRoles.forEach(role -> {
            userRolesString.add(role.getRoleName());


        });
        return UserRoleListModel.builder().userRoles(userRolesString).build();
    }


    @Transactional
    public void updateUserStatusToActive(Long authId){
        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setStatus(EStatus.ACTIVE);
        userRepository.save(user);
        List<Long> adminIds = new ArrayList<>();
        adminIds.add(1L);
        rabbitTemplate.convertAndSend("notificationExchange","notificationKey",RabbitMQNotification.builder().title("Kullanıcı Aktifleştirmeleri").userIds(adminIds).message(user.getFirstName()+" isimli "+ user.getId() + " user id'li kullanıcı Hesabını aktive etti").build());
    }

    @RabbitListener(queues = "queueActivateUserFromAuth")
    public void listenAndActivateUser(Long authId){
        updateUserStatusToActive(authId);
    }


    public List<String> getUserRoles(String jwtToken) {
        Long authId = jwtTokenManager.getUserIdFromToken(jwtToken).orElseThrow(()-> new UserException(ErrorType.INVALID_TOKEN));

        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));

        return user.getRole().stream().map(Role::getRoleName).toList();
    }
}
