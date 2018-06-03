package com.n26.statistics.api.controller;

import static com.n26.statistics.api.controller.StatisticsController.BASE_URL;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.n26.statistics.api.Utils;
import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.controller.service.StatisticsService;
import com.n26.statistics.api.controller.service.impl.StatisticBucket;

import java.time.Instant;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class StatisticsControllerTest extends BaseTests {

    @Autowired
    private Map<Long, StatisticBucket> bucketsMap;

    @Autowired
    private Utils utils;

    @Autowired
    private StatisticsService statisticsService;

    private Instant now;

    @Value("${app.transaction.timestamp.min-past-interval}")
    private int minPastInterval;

    @Before
    public void setup() {

        now = Instant.now();

        given(utils.now()).willReturn(now);
        given(utils.mapKey(notNull())).willCallRealMethod();
    }

    @Test
    public void getStatisticsForOneTransaction() throws Exception {

        bucketsMap.clear();
        statisticsService.put(new TransactionRequest(12.0, now));
        statisticsService.put(new TransactionRequest(5.0, now));
        statisticsService.put(new TransactionRequest(-5.0, now));

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(12.0)))
            .andExpect(jsonPath("$.avg", is(4.0)))
            .andExpect(jsonPath("$.max", is(12.0)))
            .andExpect(jsonPath("$.min", is(-5.0)))
            .andExpect(jsonPath("$.count", is(3)))
            .andDo(print());
    }

    @Test
    public void getEmptyStatistics() throws Exception {

        Instant pastInterval = now.minusSeconds(minPastInterval + 1);

        bucketsMap.clear();
        statisticsService.put(new TransactionRequest(12.0, pastInterval));
        statisticsService.put(new TransactionRequest(5.0, pastInterval));
        statisticsService.put(new TransactionRequest(-5.0, pastInterval));

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(0.0)))
            .andExpect(jsonPath("$.avg", is(0.0)))
            .andExpect(jsonPath("$.max", is(0.0)))
            .andExpect(jsonPath("$.min", is(0.0)))
            .andExpect(jsonPath("$.count", is(0)))
            .andDo(print());
    }

    @Test
    public void getStatisticsForTwoTransaction() throws Exception {

        bucketsMap.clear();
        statisticsService.put(new TransactionRequest(12.0, now));
        statisticsService.put(new TransactionRequest(5.0, now));
        statisticsService.put(new TransactionRequest(-5.0, now));

        Instant secondInterval = now.minusMillis(100);
        statisticsService.put(new TransactionRequest(12.0, secondInterval));
        statisticsService.put(new TransactionRequest(5.0, secondInterval));
        statisticsService.put(new TransactionRequest(-5.0, secondInterval));

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(24.0)))
            .andExpect(jsonPath("$.avg", is(4.0)))
            .andExpect(jsonPath("$.max", is(12.0)))
            .andExpect(jsonPath("$.min", is(-5.0)))
            .andExpect(jsonPath("$.count", is(6)))
            .andDo(print());
    }

    @Test
    public void getStatisticsForTenTransaction() throws Exception {

        bucketsMap.clear();

        for (int i = 0; i < 10; i++) {
            statisticsService.put(new TransactionRequest(12.0, now));
        }

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(24.0)))
            .andExpect(jsonPath("$.avg", is(4.0)))
            .andExpect(jsonPath("$.max", is(12.0)))
            .andExpect(jsonPath("$.min", is(-5.0)))
            .andExpect(jsonPath("$.count", is(6)))
            .andDo(print());
    }
}
