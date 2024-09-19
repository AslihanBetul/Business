package com.businessapi.utility;

import com.businessapi.RabbitMQ.Model.CustomerSaveFromUserModel;
import com.businessapi.dto.request.CustomerSaveDTO;
import com.businessapi.dto.request.UserSaveTestDTO;
import com.businessapi.service.CustomerService;
import com.businessapi.service.UserService;
import com.businessapi.utility.enums.EStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator {
    private final CustomerService customerService;
    private final UserService userService;

    @PostConstruct
    public void generateDemoData() {
        userDemoData();
        customerDemoData();
    }

    private void customerDemoData() {
        customerService.save(new CustomerSaveDTO("John", "Doe", "johndoe@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Jane", "Doe", "janedoe@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Bob", "Smith", "bobsmith@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Alice", "Johnson", "alicejohnson@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Tom", "Lee", "tomlee@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Sarah", "Brown", "sarahbrown@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Michael", "Davis", "michaeldavis@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Emily", "Wilson", "emilywilson@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Olivia", "Martinez", "oliviamartinez@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("William", "Anderson", "williamanderson@gmail.com", "123456789", "123 Main St", 1L));
        customerService.save(new CustomerSaveDTO("Ava", "Thomas", "avathomas@gmail.com", "123456789", "123 Main St", 1L));
    }
    private void userDemoData() {
        userService.saveUserTest(new UserSaveTestDTO(3L,1L,"serkan","guner","serkan.guner@yahoo.com"));
    }
}
