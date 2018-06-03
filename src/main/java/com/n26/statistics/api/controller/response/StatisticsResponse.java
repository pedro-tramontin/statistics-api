package com.n26.statistics.api.controller.response;

import lombok.Data;

@Data
public class StatisticsResponse {

    private final Double sum;

    private final Double avg;

    private final Double max;

    private final Double min;

    private final Long count;

}
