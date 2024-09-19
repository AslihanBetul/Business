package com.businessapi.service;



import com.businessapi.RabbitMQ.Model.CustomerReturnId;
import com.businessapi.RabbitMQ.Model.EmailResponseModel;
import com.businessapi.dto.request.CustomerSaveEmailDTO;
import com.businessapi.dto.request.SaveCustomerFromUserDTO;
import com.businessapi.dto.request.UserSaveTestDTO;
import com.businessapi.entity.Customer;
import com.businessapi.entity.User;
import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;


import com.businessapi.RabbitMQ.Model.CustomerSaveFromUserModel;
import com.businessapi.repository.UserRepository;
import com.businessapi.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final JwtTokenManager jwtTokenManager;
    private CustomerService customerService;

    @Autowired
    public void setService(@Lazy CustomerService service) {
        this.customerService = service;
    }




//    @RabbitListener(queues = "queueSaveCustomerFromUser")
//    public void save(CustomerSaveFromUserModel model) {
//        User user = userRepository.save(User.builder()
//                .authId(model.getAuthId())
//                .systemUserId(model.getUserId())
//                .build());
//        EmailResponseModel emailResponseModel = (EmailResponseModel) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyEmailFromCustomer", user.getAuthId());
//        if (emailResponseModel.getEmail() != null){
//            user.setEmail(emailResponseModel.getEmail());
//            userRepository.save(user);
//            SaveCustomerFromUserDTO saveCustomerFromUserDTO = SaveCustomerFromUserDTO.builder()
//                    .firstName(model.getFirstName())
//                    .lastName(model.getLastName())
//                    .email(emailResponseModel.getEmail())
//                    .build();
//            CustomerReturnId id = customerService.saveCustomerFromUser(saveCustomerFromUserDTO);
//            user.setCustomerId(id.getCustomerId());
//            userRepository.save(user);
//        }
//
//    }

    @RabbitListener(queues = "queueSaveCustomerFromUser")
    public void save(CustomerSaveFromUserModel model) {
        User user = userRepository.save(User.builder()
                .authId(model.getAuthId())
                .systemUserId(model.getUserId())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .build());
        EmailResponseModel emailResponseModel = (EmailResponseModel) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyEmailFromCustomer", user.getAuthId());
        if (emailResponseModel.getEmail() != null){
            user.setEmail(emailResponseModel.getEmail());
            userRepository.save(user);
        }

    }

    public void saveUserTest (UserSaveTestDTO dto){

        User user = userRepository.save(User.builder()
                .authId(dto.authId())
                .systemUserId(dto.systemUserId())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .build());
        userRepository.save(user);

    }
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new CustomerServiceException(ErrorType.USER_NOT_FOUND));
    }
    public void addCustomerId(Long id, Long customerId) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.USER_NOT_FOUND));
        user.setCustomerId(customerId);
        userRepository.save(user);
    }

    private Long getAuthIdFromToken(String token) {
        return jwtTokenManager.getIdFromToken(token).orElseThrow(()-> new CustomerServiceException(ErrorType.INVALID_TOKEN));

    }

    public Long findByAuthId(Long authid) {
        User user = userRepository.findByAuthId(authid).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
        return  user.getId();
    }

    public String createATestToken(Long authId){
       return jwtTokenManager.createToken(authId).orElseThrow(()-> new CustomerServiceException(ErrorType.INVALID_TOKEN));
    }
}
