package com.n26.statistics.api.test.config;

import com.n26.statistics.api.misc.Utils;
import com.n26.statistics.api.service.impl.StatisticBucket;

import java.util.HashMap;
import java.util.Map;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

/**
 * Mocks and objects for the tests
 */
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
