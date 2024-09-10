package com.berkhayta.analyticsservice.repository;

import com.berkhayta.analyticsservice.entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {
}

