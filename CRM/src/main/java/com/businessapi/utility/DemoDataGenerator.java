package com.businessapi.utility;


import com.businessapi.dto.request.*;
import com.businessapi.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator {
    private final CustomerService customerService;
//    private final UserService userService;
    private final MarketingCampeignService marketingCampeignService;
    private final TicketService ticketService;
    private final OpportunityService opportunityService;
    private final SalesActivityService salesActivityService;

    @PostConstruct
    public void generateDemoData() {
//        userDemoData();
        customerDemoData();
        marketingCampaignDemoData();
        ticketDemoData();
        opportunityDemoData();
        salesActivityDemoData();

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
//    private void userDemoData() {
//        userService.saveUserTest(new UserSaveTestDTO(3L,1L,"serkan","guner","serkan.guner@yahoo.com"));
//    }
    private void marketingCampaignDemoData() {
        marketingCampeignService.save(new MarketingCampaignSaveDTO("Summer Sales Boost",
                "Increase sales through summer discounts",
                LocalDateTime.of(2024, 6, 1, 0, 0),
                LocalDateTime.of(2024, 8, 31, 23, 59),
                15000.0));

        marketingCampeignService.save(new MarketingCampaignSaveDTO("Winter Sales Promotion",
                "Promote sales through winter discounts",
                LocalDateTime.of(2024, 12, 1, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59),
                10000.0));

        marketingCampeignService.save(new MarketingCampaignSaveDTO("Spring Sales Promotion",
                "Promote sales through spring discounts",
                LocalDateTime.of(2024, 3, 1, 0, 0),
                LocalDateTime.of(2024, 5, 31, 23, 59),
                12000.0));

        marketingCampeignService.save(new MarketingCampaignSaveDTO("Fall Sales Promotion",
                "Promote sales through fall discounts",
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 11, 30, 23, 59),
                18000.0));

        marketingCampeignService.save(new MarketingCampaignSaveDTO("Christmas Sales Promotion",
                "Promote sales through Christmas discounts",
                LocalDateTime.of(2024, 12, 25, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59),
                20000.0));
    }

    private void ticketDemoData() {
        ticketService.save(new TicketSaveDTO(101L,
                "Login Issue",
                "Customer unable to login to their account",
                "OPEN",
                "HIGH",
                LocalDateTime.of(2024, 9, 1, 10, 0),
                null));

        ticketService.save(new TicketSaveDTO(102L,
                "Payment Issue",
                "Customer unable to make payment",
                "OPEN",
                "MEDIUM",
                LocalDateTime.of(2024, 9, 1, 11, 0),
                null));

        ticketService.save(new TicketSaveDTO(103L,
                "Order Issue",
                "Customer unable to place order",
                "OPEN",
                "LOW",
                LocalDateTime.of(2024, 9, 1, 12, 0),
                null));

        ticketService.save(new TicketSaveDTO(104L,
                "Shipping Issue",
                "Customer unable to ship order",
                "OPEN",
                "HIGH",
                LocalDateTime.of(2024, 9, 1, 13, 0),
                null));

        ticketService.save(new TicketSaveDTO(105L,
                "Return Issue",
                "Customer unable to return order",
                "OPEN",
                "MEDIUM",
                LocalDateTime.of(2024, 9, 1, 14, 0),
                null));

    }

    private void opportunityDemoData() {
        opportunityService.save(new OpportunitySaveDTO(201L,
                "New Website Development",
                "Develop a new website for the client",
                50000.0,
                "Proposal Sent",
                0.75,
                301L));

        opportunityService.save(new OpportunitySaveDTO(202L,
                "Mobile App Development",
                "Develop a new mobile app for the client",
                80000.0,
                "Proposal Sent",
                0.75,
                302L));

        opportunityService.save(new OpportunitySaveDTO(203L,
                "Software Development",
                "Develop a new software for the client",
                100000.0,
                "Proposal Sent",
                0.75,
                303L));

        opportunityService.save(new OpportunitySaveDTO(204L,
                "Hardware Repair",
                "Repair hardware for the client",
                30000.0,
                "Proposal Sent",
                0.75,
                304L));

        opportunityService.save(new OpportunitySaveDTO(205L,
                "Software Upgrade",
                "Upgrade software for the client",
                60000.0,
                "Proposal Sent",
                0.75,
                305L));
    }

    private void  salesActivityDemoData(){
        salesActivityService.save(new SalesActivitySaveDTO( 301L,
                "Initial Meeting",
                LocalDate.of(2024, 9, 1),
                "Discussed client's requirements and project scope"));

        salesActivityService.save(new SalesActivitySaveDTO( 302L,
                "Proposal Sent",
                LocalDate.of(2024, 9, 2),
                "Sent proposal to client"));

        salesActivityService.save(new SalesActivitySaveDTO( 303L,
                "Proposal Accepted",
                LocalDate.of(2024, 9, 3),
                "Accepted proposal"));

        salesActivityService.save(new SalesActivitySaveDTO( 304L,
                "Proposal Rejected",
                LocalDate.of(2024, 9, 4),
                "Rejected proposal"));

        salesActivityService.save(new SalesActivitySaveDTO( 305L,
                "Contract Signed",
                LocalDate.of(2024, 9, 5),
                "Signed contract"));


    }




}
