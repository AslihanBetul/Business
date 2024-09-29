package com.businessapi.service;

import com.businessapi.entity.Plan;
import com.businessapi.entity.enums.ERoles;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.SubscriptionServiceException;
import com.businessapi.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    public Plan save(Plan plan) {
        return planRepository.save(plan);
    }

    public Plan findById(Long id) {
        return planRepository.findById(id).orElseThrow(() -> new SubscriptionServiceException(ErrorType.SUBSCRIPTION_NOT_FOUND));
    }

    public Boolean delete(Long id) {
        Plan plan = findById(id);
        plan.setStatus(EStatus.DELETED);
        planRepository.save(plan);
        return true;
    }

    public List<ERoles> getRolesByPlanIds(List<Long> planIds) {
        return planRepository
                .findAllById(planIds)
                .stream()
                .map(Plan::getRoles)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
