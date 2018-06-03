package com.n26.statistics.api.test.controller;

import static com.n26.statistics.api.test.utils.FakeData.randomAmount;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.Assert.isTrue;

import com.n26.statistics.api.controller.TransactionController;
import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.service.impl.StatisticBucket;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionControllerTest extends BaseTests {

    @Autowired
    private Map<Long, StatisticBucket> buckets;

    @Test
    public void validTransaction() throws Exception {

        buckets.clear();
        TransactionRequest request = new TransactionRequest(randomAmount(), Instant.now());

        super.mvc.perform(post(TransactionController.BASE_URL)
            .contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document(REST_DOCS_DIR));

        isTrue(buckets.size() == 1, "buckets should have 1 item");
    }

    @Test
    public void invalidTransaction() throws Exception {

        buckets.clear();

        Instant invalidTimestamp = Instant.now().minus(60, ChronoUnit.SECONDS);
        TransactionRequest request = new TransactionRequest(randomAmount(), invalidTimestamp);

        super.mvc.perform(post(TransactionController.BASE_URL)
            .contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document(REST_DOCS_DIR));

        isTrue(buckets.size() == 0, "buckets should be empty");
    }
}
