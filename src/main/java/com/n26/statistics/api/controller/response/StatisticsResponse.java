package com.n26.statistics.api.controller.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class StatisticsResponse {

    private final BigDecimal sum;

    private final BigDecimal avg;

    private final BigDecimal max;

    private final BigDecimal min;

    private final Long count;

}
