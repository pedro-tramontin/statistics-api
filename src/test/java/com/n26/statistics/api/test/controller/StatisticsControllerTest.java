package com.n26.statistics.api.test.controller;

import static com.n26.statistics.api.controller.StatisticsController.BASE_URL;
import static com.n26.statistics.api.test.utils.FakeData.randomAmount;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.Assert.isTrue;

import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.misc.Utils;
import com.n26.statistics.api.service.StatisticsService;
import com.n26.statistics.api.service.impl.StatisticBucket;

import java.time.Instant;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class StatisticsControllerTest extends BaseTests {

    @Autowired
    private Map<Long, StatisticBucket> buckets;

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
        given(utils.toBigDecimalScaledToTwo(notNull())).willCallRealMethod();
    }

    @Test
    public void emptyStatistics() throws Exception {

        buckets.clear();

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(0.00)))
            .andExpect(jsonPath("$.avg", is(0.00)))
            .andExpect(jsonPath("$.max", is(0.00)))
            .andExpect(jsonPath("$.min", is(0.00)))
            .andExpect(jsonPath("$.count", is(0)))
            .andDo(print());

        isTrue(buckets.size() == 0, "buckets should be empty");
    }

    @Test
    public void oneTransaction() throws Exception {

        buckets.clear();

        double amount = randomAmount();
        statisticsService.put(new TransactionRequest(amount, now));

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(amount)))
            .andExpect(jsonPath("$.avg", is(amount)))
            .andExpect(jsonPath("$.max", is(amount)))
            .andExpect(jsonPath("$.min", is(amount)))
            .andExpect(jsonPath("$.count", is(1)))
            .andDo(print());

        isTrue(buckets.size() == 1, "buckets should have 1 item");
    }

    @Test
    public void threeTransaction() throws Exception {

        buckets.clear();

        double amount1 = randomAmount();
        double amount2 = randomAmount();

        statisticsService.put(new TransactionRequest(amount1, now));
        statisticsService.put(new TransactionRequest(amount2, now));
        statisticsService.put(new TransactionRequest(-amount2, now));

        double expSum = utils.toBigDecimalScaledToTwo(amount1 + amount2 - amount2).doubleValue();
        double expMax = Math.max(Math.max(amount1, amount2), -amount2);
        double expMin = Math.min(Math.min(amount1, amount2), -amount2);
        int expCount = 3;
        double expAvg = utils.toBigDecimalScaledToTwo(expSum / expCount).doubleValue();

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(expSum)))
            .andExpect(jsonPath("$.avg", is(expAvg)))
            .andExpect(jsonPath("$.max", is(expMax)))
            .andExpect(jsonPath("$.min", is(expMin)))
            .andExpect(jsonPath("$.count", is(expCount)))
            .andDo(print());

        isTrue(buckets.size() == 1, "buckets should have 1 item");
    }

    @Test
    public void rounding() throws Exception {

        buckets.clear();
        for (int i = 0; i < 10; i++) {
            statisticsService.put(new TransactionRequest(12.30, now));
        }

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(123.00)))
            .andExpect(jsonPath("$.avg", is(12.30)))
            .andExpect(jsonPath("$.max", is(12.30)))
            .andExpect(jsonPath("$.min", is(12.30)))
            .andExpect(jsonPath("$.count", is(10)))
            .andDo(print());

        isTrue(buckets.size() == 1, "buckets should have 1 item");
    }

    @Test
    public void differentBuckets() throws Exception {

        buckets.clear();

        double expSum = 0.0;
        double expMax = -5000.0;
        double expMin = 5000.0;

        for (int i = 0; i < 10; i++) {

            double amount = randomAmount();
            Instant instant = now.minusSeconds(i);

            statisticsService.put(new TransactionRequest(amount, instant));

            expSum += amount;
            expMax = Math.max(expMax, amount);
            expMin = Math.min(expMin, amount);
        }

        expSum = utils.toBigDecimalScaledToTwo(expSum).doubleValue();
        expMax = utils.toBigDecimalScaledToTwo(expMax).doubleValue();
        expMin = utils.toBigDecimalScaledToTwo(expMin).doubleValue();
        int count = 10;
        double expAvg = utils.toBigDecimalScaledToTwo(expSum / count).doubleValue();

        super.mvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(expSum)))
            .andExpect(jsonPath("$.avg", is(expAvg)))
            .andExpect(jsonPath("$.max", is(expMax)))
            .andExpect(jsonPath("$.min", is(expMin)))
            .andExpect(jsonPath("$.count", is(count)))
            .andDo(print());

        isTrue(buckets.size() == 10, "buckets should have 10 items");
    }
}
