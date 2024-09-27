package com.businessapi.service;

import com.businessapi.RabbitMQ.Model.AddRoleFromSubscriptionModel;
import com.businessapi.dto.request.SubscriptionSaveRequestDTO;
import com.businessapi.entity.Plan;
import com.businessapi.entity.Subscription;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.entity.enums.ESubscriptionType;
import com.businessapi.repository.SubscriptionRepository;
import com.businessapi.util.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import com.businessapi.util.SessionManager;
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanService planService;
    private final RabbitTemplate rabbitTemplate;
    public Subscription save(SubscriptionSaveRequestDTO dto) {
        Long id = SessionManager.getMemberIdFromAuthenticatedMember();
        Subscription subscription = Subscription.builder()
                .userId(id)
                .planId(dto.planId())
                .startDate(LocalDate.now())
                .build();
        if(dto.subscriptionType().equals(ESubscriptionType.MONTHLY)) {
            subscription.setEndDate(subscription.getStartDate().plusMonths(1));
        } else if(dto.subscriptionType().equals(ESubscriptionType.YEARLY)) {
            subscription.setEndDate(subscription.getStartDate().plusYears(1));
        }
        subscription.setStatus(EStatus.ACTIVE);
        Plan plan = planService.findById(dto.planId());

        System.out.println(plan.getRoles());
        rabbitTemplate.convertAndSend(
                "businessDirectExchange",
                "keyAddRoleFromSubscription",
                AddRoleFromSubscriptionModel.builder()
                        .authId(id)
                        .roles(plan.getRoles().stream()
                                .map(Object::toString)
                                .collect(Collectors.toList())
                        )
                        .build()
        );

        // TODO: Send email
        System.out.println(subscription.toString());
        return subscriptionRepository.save(subscription);
    }
}
