package com.businessapi.service;

import com.businessapi.dto.request.PlanSaveRequestDTO;
import com.businessapi.dto.request.PlanUpdateRequestDTO;
import com.businessapi.dto.response.PlanGetResponseDTO;
import com.businessapi.entity.Plan;
import com.businessapi.entity.PlanTranslation;
import com.businessapi.entity.enums.ERoles;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.SubscriptionServiceException;
import com.businessapi.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanTranslationService planTranslationService;
    public List<PlanGetResponseDTO> findAll(String language) {
        List<Plan> planList = planRepository.findAllByStatusNot(EStatus.DELETED);
        System.out.println("language: " + language);
        return planList.stream().map(plan -> {
            Optional<PlanTranslation> optionalTranslation = plan.getTranslations().stream().filter(t -> t.getLanguage().equals(language)).findFirst();
            PlanTranslation translation;
            if(optionalTranslation.isPresent()){
                translation = optionalTranslation.get();
            } else if (plan.getTranslations().stream().filter(t -> t.getLanguage().equals("tr")).findFirst().isPresent()) {
                translation = plan.getTranslations().stream().filter(t -> t.getLanguage().equals("tr")).findFirst().get();
            } else {
                throw new RuntimeException("No translation found for plan: " + plan.getId());
            }
            System.out.println(translation);
            return new PlanGetResponseDTO(
                    plan.getId(),
                    plan.getPrice(),
                    plan.getRoles(),
                    translation.getDescription(),
                    translation.getName()
            );
        }).collect(Collectors.toList());
    }

    public Plan save(PlanSaveRequestDTO dto) {
        Plan plan = Plan.builder()
                .price(dto.price())
                .roles(dto.roles())
                .build();
        planRepository.save(plan);
        List<PlanTranslation> translations = new ArrayList<>();
        dto.translations().forEach(t -> {
            PlanTranslation translation = planTranslationService.save(PlanTranslation.builder().name(t.getName()).description(t.getDescription()).planId(plan.getId()).language(t.getLanguage()).build());
            translations.add(translation);
        });
        plan.setTranslations(translations);
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

    public Plan update(PlanUpdateRequestDTO dto) {
        Plan plan = findById(dto.id());
        plan.setPrice(dto.price());
        plan.setRoles(dto.roles());
        dto.translations().forEach(t -> {
            PlanTranslation planTranslation = planTranslationService.findById(t.getId());
            planTranslation.setDescription(t.getDescription());
            planTranslation.setName(t.getName());
            planTranslationService.save(planTranslation);
        });
        return planRepository.save(plan);
    }
}
