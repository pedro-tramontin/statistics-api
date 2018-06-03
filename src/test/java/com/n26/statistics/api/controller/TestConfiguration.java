package com.n26.statistics.api.controller;

import com.n26.statistics.api.Utils;
import com.n26.statistics.api.controller.service.impl.StatisticBucket;

import java.util.HashMap;
import java.util.Map;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfiguration {

    @Bean
    @Primary
    public Map<Long, StatisticBucket> bucketsMap() {
        return new HashMap<>();
    }

    @Bean
    @Primary
    public Utils mockUtils() {
        return Mockito.mock(Utils.class);
    }

}
