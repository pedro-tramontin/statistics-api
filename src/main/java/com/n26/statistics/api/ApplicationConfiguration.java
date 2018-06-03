package com.n26.statistics.api;

import com.n26.statistics.api.controller.service.impl.StatisticBucket;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Map<Long, StatisticBucket> bucketsMap() {
        return new HashMap<>();
    }

}
