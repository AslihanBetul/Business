package com.businessapi.service;

import com.businessapi.RabbitMQ.Model.AddRoleFromSubscriptionModel;
import com.businessapi.dto.request.SubscriptionHistoryRequestDto;
import com.businessapi.dto.request.SubscriptionSaveRequestDTO;
import com.businessapi.entity.Plan;
import com.businessapi.entity.Subscription;
import com.businessapi.entity.enums.ERoles;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.entity.enums.ESubscriptionType;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.SubscriptionServiceException;
import com.businessapi.repository.SubscriptionRepository;
import com.businessapi.util.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanService planService;
    private final RabbitTemplate rabbitTemplate;

    public List<Subscription> findUsersSubscriptionsByAuthId(Long authId) {
        return subscriptionRepository.findAllByAuthIdAndStatusNot(authId, EStatus.ENDED);
    }

    public Subscription subscribe(SubscriptionSaveRequestDTO dto) {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Subscription> activeSubscriptions = findUsersSubscriptionsByAuthId(authId);
        List<Plan> plans = activeSubscriptions.stream().map(Subscription::getPlan).toList();
        Set<ERoles> uniqueRoles = new HashSet<>(plans.stream().map(Plan::getRoles).flatMap(List::stream).toList());

        Subscription subscription = Subscription.builder()
                .authId(authId)
                .plan(planService.findById(dto.planId()))
                .startDate(LocalDateTime.now())
                .build();
        if(dto.subscriptionType().equals(ESubscriptionType.MONTHLY)) {
            subscription.setEndDate(subscription.getStartDate().plusMinutes(2));
        } else if(dto.subscriptionType().equals(ESubscriptionType.YEARLY)) {
            subscription.setEndDate(subscription.getStartDate().plusYears(1));
        }

        uniqueRoles.addAll(planService.findById(dto.planId()).getRoles());
        rabbitTemplate.convertAndSend(
                "businessDirectExchange",
                "keyAddRoleFromSubscription",
                AddRoleFromSubscriptionModel.builder()
                        .authId(authId)
                        .roles(uniqueRoles.stream().map(ERoles::name).collect(Collectors.toList()))
                        .build()
        );

        // TODO: Send email
        System.out.println(subscription.toString());
        return subscriptionRepository.save(subscription);
    }

    public Boolean unsubscribe (Long planId) {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();
        Subscription subscription = subscriptionRepository.findByPlanIdAndAuthIdAndStatus(planId, authId, EStatus.ACTIVE).orElseThrow(() -> new SubscriptionServiceException(ErrorType.SUBSCRIPTION_NOT_FOUND));
        subscription.setStatus(EStatus.CANCELLED);
        subscriptionRepository.save(subscription);
        return true;
    }

    public List<String> checkSubscriptions () {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Subscription> subscriptions = subscriptionRepository.findAllByAuthIdAndStatusNot(authId, EStatus.ENDED);
        subscriptions.forEach(subscription -> {
            if(subscription.getEndDate().isBefore(LocalDateTime.now())) {
                subscription.setStatus(EStatus.ENDED);
                subscriptionRepository.save(subscription);
            }
        });
        List<Subscription> activeSubscriptions = subscriptionRepository.findAllByAuthIdAndStatusNot(authId, EStatus.ENDED);
        List<Plan> plans = activeSubscriptions.stream().map(Subscription::getPlan).toList();
        Set<ERoles> uniqueRoles = new HashSet<>(plans.stream().map(Plan::getRoles).flatMap(List::stream).toList());
        rabbitTemplate.convertAndSend(
                "businessDirectExchange",
                "keyAddRoleFromSubscription",
                AddRoleFromSubscriptionModel.builder()
                        .authId(authId)
                        .roles(uniqueRoles.stream().map(ERoles::name).collect(Collectors.toList()))
                        .build()
        );
        return getActiveSubscriptionRoles(authId);
    }

    private List<String> getActiveSubscriptionRoles(Long authId) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByAuthIdAndStatus(authId, EStatus.ACTIVE);
        List<Plan> plans = subscriptions.stream().map(Subscription::getPlan).toList();
        Set<ERoles> uniqueRoles = new HashSet<>(plans.stream().map(Plan::getRoles).flatMap(List::stream).toList());
        return uniqueRoles.stream().map(ERoles::name).collect(Collectors.toList());
    }

    public void subscribeToPlanForDemoData (Long authId, Long planId, int year) {
        subscriptionRepository.save(Subscription.builder().authId(authId).plan(planService.findById(planId)).startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusYears(year)).build());
    }

    public List<SubscriptionHistoryRequestDto> getSubscriptionHistory(String language) {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Subscription> subscriptions = subscriptionRepository.findAllByAuthId(authId);
        return subscriptions.stream().map(subscription -> subscription.toSubscriptionHistoryRequestDto(language)).collect(Collectors.toList());
    }
}
