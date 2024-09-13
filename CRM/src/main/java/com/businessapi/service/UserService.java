package com.businessapi.service;



import com.businessapi.dto.request.CustomerSaveEmailDTO;
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
        userRepository.save(User.builder()
                .authId(model.getAuthId())
                .userId(model.getUserId())
                .build());

        return true;
    }


    // This method will send auth info request from customer
    public void sendAuthRequestFromUser(String token) {
        Long authId = getAuthIdFromToken(token);
        rabbitTemplate.convertAndSend("businessDirectExchange", "keyRequestCustomerFromAuth", authId);
    }

    // This method will send email request from customer/user to auth and save email
    @RabbitListener(queues = "queueEmailFromAuth")
    public boolean saveEmail(CustomerSaveEmailDTO customerSaveEmailDTO) {
        // token ile auth dan email istemek icin kuyruk olusturuldu
        sendAuthRequestFromUser(customerSaveEmailDTO.token());
        // user bulunabilmesi icin authid si bulundu
        Long authId = getAuthIdFromToken(customerSaveEmailDTO.token());
        // user olusturuldu
        User user = findByAuthId(authId);
        // email eklendi
        user.setEmail(customerSaveEmailDTO.email());
        userRepository.save(user);
        return true;
    }

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
