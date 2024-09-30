package com.businessapi.util;
import com.businessapi.entity.Plan;
import com.businessapi.entity.enums.ERoles;
import com.businessapi.service.PlanService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator {
    private final PlanService planService;
    @PostConstruct
    public void generateDemoData() {
        planDemoData();
    }

    private void planDemoData() {
        planService.save(Plan.builder().name("pmm").price(100.0).roles(List.of(ERoles.PMM)).build());
        planService.save(Plan.builder().name("crmm").price(200.0).roles(List.of(ERoles.CRMM)).build());
        planService.save(Plan.builder().name("imm").price(300.0).roles(List.of(ERoles.IMM)).build());
        planService.save(Plan.builder().name("hrmm").price(300.0).roles(List.of(ERoles.HRMM)).build());
        planService.save(Plan.builder().name("fam").price(300.0).roles(List.of(ERoles.FAM)).build());
        planService.save(Plan.builder().name("oam").price(300.0).roles(List.of(ERoles.OAM)).build());
    }
}