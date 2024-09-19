package com.businessapi.repository;

import com.businessapi.entity.MarketingCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketingCampeignRepository extends JpaRepository<MarketingCampaign, Long> {
}
