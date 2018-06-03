package com.n26.statistics.api.controller;

import static com.n26.statistics.api.controller.StatisticsController.BASE_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.n26.statistics.api.Utils;
import com.n26.statistics.api.controller.response.StatisticsResponse;
import com.n26.statistics.api.controller.service.StatisticsService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BASE_URL, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StatisticsController {

    public static final String BASE_URL = "/statistics";

    private final StatisticsService statisticsService;

    private final Utils utils;

    @GetMapping
    public StatisticsResponse get() {

        return statisticsService.getStatistics(utils.now());
    }
}
