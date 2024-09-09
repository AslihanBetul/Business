package com.bilgeadam.utility;

import com.bilgeadam.dto.request.CustomerSaveDTO;
import com.bilgeadam.dto.request.CustomerSaveDemoDTO;
import com.bilgeadam.service.CustomerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator {
    private final CustomerService customerService;

    @PostConstruct
    public void generateDemoData() {
        customerDemoData();
    }

    private void customerDemoData() {
        customerService.save(new CustomerSaveDTO("John", "Doe", "johndoe@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Jane", "Doe", "janedoe@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Bob", "Smith", "bobsmith@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Alice", "Johnson", "alicejohnson@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Tom", "Lee", "tomlee@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Sarah", "Brown", "sarahbrown@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Michael", "Davis", "michaeldavis@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Emily", "Wilson", "emilywilson@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Olivia", "Martinez", "oliviamartinez@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("William", "Anderson", "williamanderson@gmail.com", "123456789", "123 Main St"));
        customerService.save(new CustomerSaveDTO("Ava", "Thomas", "avathomas@gmail.com", "123456789", "123 Main St"));
    }
}
