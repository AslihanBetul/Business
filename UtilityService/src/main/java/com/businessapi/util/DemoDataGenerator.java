package com.businessapi.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator
{

    @PostConstruct
    public void generateDemoData()
    {

    }
}
