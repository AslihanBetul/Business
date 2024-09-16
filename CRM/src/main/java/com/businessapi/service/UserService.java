package com.businessapi.service;



import com.businessapi.RabbitMQ.Model.CustomerReturnId;
import com.businessapi.RabbitMQ.Model.EmailResponseModel;
import com.businessapi.dto.request.CustomerSaveEmailDTO;
import com.businessapi.dto.request.UserSaveTestDTO;
import com.businessapi.entity.User;
import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;


import com.businessapi.RabbitMQ.Model.CustomerSaveFromUserModel;
import com.businessapi.repository.UserRepository;
import com.businessapi.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final JwtTokenManager jwtTokenManager;




    @RabbitListener(queues = "queueSaveCustomerFromUser")
    public boolean save(CustomerSaveFromUserModel model) {
        User user = userRepository.save(User.builder()
                .authId(model.getAuthId())
                .userId(model.getUserId())
                .build());

        EmailResponseModel emailResponseModel = (EmailResponseModel) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyEmailFromCustomer", user.getAuthId());
        if (emailResponseModel.getEmail() != null){
            user.setEmail(emailResponseModel.getEmail());
            userRepository.save(user);
            CustomerReturnId customerReturnId = (CustomerReturnId) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keySaveCustomerByEmail", model);
            user.setCustomerId(customerReturnId.getCustomerId());
            userRepository.save(user);
            return true;
        }else {
            return false;
        }

    }

    public boolean saveUserTest (UserSaveTestDTO dto){

        User user = userRepository.save(User.builder()
                .authId(dto.authId())
                .userId(dto.userId())
                .email(dto.email())
                .build());
        userRepository.save(user);
        EmailResponseModel model = EmailResponseModel.builder().email(dto.email()).build();
        CustomerReturnId customerReturnId = (CustomerReturnId) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keySaveCustomerByEmail", model);
        user.setCustomerId(customerReturnId.getCustomerId());
        userRepository.save(user);
        return true;
    }












    // This method will send email request from customer/user to auth and save email

//    public boolean saveEmail(Long authId) {
//        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
//        EmailResponseModel model = (EmailResponseModel) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyEmailFromAuth", authId);
//        // email eklendi
//        user.setEmail(model.getEmail());
//        userRepository.save(user);
//        return true;
//    }

    //
    private Long getAuthIdFromToken(String token) {
        return jwtTokenManager.getIdFromToken(token).orElseThrow(()-> new CustomerServiceException(ErrorType.INVALID_TOKEN));

    }


    public User findByAuthId(Long authid) {
        return userRepository.findByAuthId(authid).orElseThrow(()-> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
    }

    public Long findByAuthIdForCustomer(Long authid) {
        User user = userRepository.findByAuthId(authid).orElseThrow(() -> new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER));
        return  user.getCustomerId();
    }
}
