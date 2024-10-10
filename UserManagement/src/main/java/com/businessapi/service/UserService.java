package com.businessapi.service;

import com.businessapi.RabbitMQ.Model.*;
import com.businessapi.dto.requestDTOs.*;
import com.businessapi.dto.responseDTOs.GetAllUsersResponseDTO;
import com.businessapi.dto.responseDTOs.GetUserInformationDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        if(!userSaveRequestDTO.roleIds().isEmpty()){
            List<Role> usersRoles = roleService.getRolesByRoleId(userSaveRequestDTO.roleIds());
            user.setRole(usersRoles);
        }else {
            user.setRole(new ArrayList<>());
        }

        user.setStatus(EStatus.ACTIVE);

        Long authId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keySaveAuthFromUser", SaveAuthFromUserModel.builder().email(userSaveRequestDTO.email()).password(userSaveRequestDTO.password()).build());
        user.setAuthId(authId);

        userRepository.save(user);
        isUserCustomer(user);
    }



    @RabbitListener(queues = "queueSaveUserFromOtherServices")
    public Long saveUserFromOtherServices(SaveUserFromOtherServicesModel saveUserFromOtherServicesModel) {
        User user = UserMapper.INSTANCE.saveUserFromOtherServicesToUser(saveUserFromOtherServicesModel);
        List<Role> userRoles = new ArrayList<>();
        if(!(roleService.checkIfRoleExistsByRoleName(saveUserFromOtherServicesModel.getRole()))){
            Role role = roleService.saveRole(Role.builder().roleName(saveUserFromOtherServicesModel.getRole().toUpperCase()).build());
            userRoles.add(role);
            user.setRole(userRoles);
        } else{
            Role role = roleService.findByRoleName(saveUserFromOtherServicesModel.getRole());
            userRoles.add(role);
            user.setRole(userRoles);
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

        if(!user.getFirstName().equals(userUpdateRequestDTO.firstName())){
            user.setFirstName(userUpdateRequestDTO.firstName());
        }
        if(!user.getLastName().equals(userUpdateRequestDTO.lastName())){
            user.setLastName(userUpdateRequestDTO.lastName());
        }

        sendUserMailToAuthService(AuthMailUpdateFromUser.builder().authId(user.getAuthId()).email(userUpdateRequestDTO.email()).build());
        userRepository.save(user);
    }

    private void sendUserMailToAuthService(AuthMailUpdateFromUser authMailUpdateFromUser) {
        rabbitTemplate.convertAndSend("businessDirectExchange","keyAuthMailUpdateFromUser", authMailUpdateFromUser);
    }


    @Transactional
    public void saveUserFromAuthService(SaveUserFromAuthModel saveUserFromAuthModel){
        List<Role> usersRoles = new ArrayList<>();
        usersRoles.add(roleService.getRoleById(3L)); //MEMBER rol olarak kaydedilir.
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
        allUsersList = allUsersList.stream()
                .filter(user -> user.getRole().stream().noneMatch(role -> role.getRoleName().equals("SUPER_ADMIN")))
                .toList();

        List<GetAllUsersResponseDTO> allUsersResponseDTOList = new ArrayList<>();

       allUsersList.forEach(user -> {
           List<String> userRolesString = user.getRole().stream().map(Role::getRoleName).toList();
           String usersMail = (String) rabbitTemplate.convertSendAndReceive("businessDirectExchange","keyGetMailByAuthId", user.getAuthId());
           allUsersResponseDTOList.add(GetAllUsersResponseDTO.builder()
                   .id(user.getId())
                   .firstName(user.getFirstName())
                   .lastName(user.getLastName())
                   .email(usersMail)
                   .status(user.getStatus())
                   .userRoles(userRolesString)
                   .build());
       });


        return allUsersResponseDTOList;
    }


    public List<User> pagableGettAll(PageRequestDTO pageRequestDTO){

        return userRepository.findAllByLastNameContainingIgnoreCase(pageRequestDTO.searchText(), PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size()));

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
        Long authId = jwtTokenManager.getAuthIdFromToken(jwtToken).orElseThrow(()-> new UserException(ErrorType.INVALID_TOKEN));

        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));

        return user.getRole().stream().map(Role::getRoleName).toList();
    }

    public GetUserInformationDTO getUserInformation(String jwtToken) {
        Long authId = jwtTokenManager.getAuthIdFromToken(jwtToken).orElseThrow(()-> new UserException(ErrorType.INVALID_TOKEN));
        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));

        String usersMail = (String) rabbitTemplate.convertSendAndReceive("businessDirectExchange","keyGetMailByAuthId", user.getAuthId());


        return GetUserInformationDTO.builder().id(user.getId()).authId(user.getAuthId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(usersMail).build();
    }

    @RabbitListener(queues = "queueAddRoleFromSubscription")
    public void addRoleFromSubscription(AddRoleFromSubscriptionModel addRoleFromSubscriptionModel) {
        User user = userRepository.findByAuthId(addRoleFromSubscriptionModel.getAuthId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setRole(new ArrayList<>()); //Kullanıcının rollerini sıfırlamak için yenmi bir liste tanımlanır.
        user.getRole().add(roleService.findByRoleName("MEMBER")); //Kullanıcıya MEMBER rol tanımlanır.

        // if addRoleFromSubscriptionModel.roles is not empty then add roles
        if(!addRoleFromSubscriptionModel.getRoles().isEmpty()){
            addRoleFromSubscriptionModel.getRoles().forEach(roleName -> {
                Role role = roleService.findByRoleName(roleName);
                user.getRole().add(role);
            });
        }
        // if addRoleFromSubscriptionModel.roles is empty just save it empty
        userRepository.save(user);
    }
    @RabbitListener(queues = "queueDeleteRoleFromSubscription")
    public void deleteRoleFromSubscription(DeleteRoleFromSubscriptionModel deleteRoleFromSubscriptionModel) {
        User user = userRepository.findByAuthId(deleteRoleFromSubscriptionModel.getAuthId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        if(deleteRoleFromSubscriptionModel.getRoles().isEmpty()){
            throw new UserException(ErrorType.ROLE_LIST_IS_EMPTY);
        }

        deleteRoleFromSubscriptionModel.getRoles().forEach(roleName -> {
            Role role = roleService.findByRoleName(roleName);
            user.getRole().remove(role);
        });
        userRepository.save(user);

    }


    @Transactional
    public Boolean changeUserEmail(ChangeUserEmailRequestDTO changeUserEmailRequestDTO) {

        User user = userRepository.findById(changeUserEmailRequestDTO.id()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));

        sendUserMailToAuthService(AuthMailUpdateFromUser.builder().authId(user.getAuthId()).email(changeUserEmailRequestDTO.email()).build());


        return true;
    }

    public Boolean changeUserPassword(ChangeUserPassword changeUserPassword) {
        User user = userRepository.findById(changeUserPassword.userId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        String userNewPassword = passwordGenerator();
        rabbitTemplate.convertAndSend("businessDirectExchange","keyChangePasswordFromUser",ChangePasswordFromUserModel.builder().authId(user.getAuthId()).newPassword(userNewPassword).build());
        String usersMail = (String) rabbitTemplate.convertSendAndReceive("businessDirectExchange","keyGetMailByAuthId", user.getAuthId());
        rabbitTemplate.convertAndSend("businessDirectExchange","keySendMailNewPassword",SendMailNewPasswordModel.builder().newPassword(userNewPassword).email(usersMail).build());
        return true;
    }
    public String passwordGenerator(){
        String codeSource= UUID.randomUUID().toString();
        String[] splitCodeSource = codeSource.split("-");
        StringBuilder code= new StringBuilder();
        for (String s : splitCodeSource) {
            code.append(s.charAt(0));
        }
        return code.toString();
    }

    @Transactional
    public Boolean updateUserStatus(UpdateUserStatusRequestDTO updateUserStatusRequestDTO) {
        User user = userRepository.findById(updateUserStatusRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setStatus(updateUserStatusRequestDTO.status());
        rabbitTemplate.convertAndSend("businessDirectExchange", "keyUpdateStatus",UpdateStatusModel.builder().authId(user.getAuthId()).status(user.getStatus()).build());
        userRepository.save(user);

        return null;
    }

    @RabbitListener(queues = "queueGetUserIdByToken")
    public Long getUserIdByToken(String token) {

        //String jwtToken = token.replace("Bearer ", ""); eğer token Normal gelmezse yalın hale getirmek için bunu yorum satırından kaldırın daha sonra jwtToken değişkenini token değişkeni yerine bir alt satırdaki Long authId = jwtTokenManager.getAuthIdFromToken(token).orElseThrow(()->new UserException(ErrorType.INVALID_TOKEN)); metodunda kullanın

        Long authId = jwtTokenManager.getAuthIdFromToken(token).orElseThrow(()->new UserException(ErrorType.INVALID_TOKEN));

        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));

        return user.getId();
    }


}
