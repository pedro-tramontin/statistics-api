package com.n26.statistics.api.config;

import com.n26.statistics.api.service.impl.StatisticBucket;

import com.google.common.base.Predicates;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ApplicationConfiguration {

    /**
     * Uses bucketAtInstantLessSecond ConcurrentHashMap to store the statistics buckets
     */
    @Bean
    public Map<Long, StatisticBucket> bucketsMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Docket to generate Swagger documentation for the API
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(Predicates.not(PathSelectors.regex("/error")))
            .build();
    }
}
