package com.businessapi.service;

import com.businessapi.RabbitMQ.Model.AddRoleFromSubscriptionModel;
import com.businessapi.dto.request.SubscriptionSaveRequestDTO;
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
        return subscriptionRepository.findAllByAuthIdAndStatusNot(authId, EStatus.DELETED);
    }

    public Subscription subscribe(SubscriptionSaveRequestDTO dto) {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Subscription> activeSubscriptions = findUsersSubscriptionsByAuthId(authId);
        List<Long> planIds = activeSubscriptions.stream().map(Subscription::getPlanId).toList();
        Set<ERoles> uniqueRoles = new HashSet<>(planService.getRolesByPlanIds(planIds));

        Subscription subscription = Subscription.builder()
                .authId(authId)
                .planId(dto.planId())
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
        subscription.setStatus(EStatus.INACTIVE);
        subscriptionRepository.save(subscription);
        return true;
    }

    public List<String> checkSubscriptions () {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Subscription> subscriptions = subscriptionRepository.findAllByAuthIdAndStatusNot(authId, EStatus.DELETED);
        subscriptions.forEach(subscription -> {
            if(subscription.getEndDate().isBefore(LocalDateTime.now())) {
                subscription.setStatus(EStatus.DELETED);
                subscriptionRepository.save(subscription);
            }
        });
        List<Subscription> activeSubscriptions = subscriptionRepository.findAllByAuthIdAndStatusNot(authId, EStatus.DELETED);
        List<Long> planIds = activeSubscriptions.stream().map(Subscription::getPlanId).toList();
        Set<ERoles> uniqueRoles = new HashSet<>(planService.getRolesByPlanIds(planIds));
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
        List<Long> planIds = subscriptions.stream().map(Subscription::getPlanId).toList();
        Set<ERoles> uniqueRoles = new HashSet<>(planService.getRolesByPlanIds(planIds));
        return uniqueRoles.stream().map(ERoles::name).collect(Collectors.toList());
    }
}
