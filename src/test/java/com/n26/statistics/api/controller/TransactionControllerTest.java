package com.n26.statistics.api.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.n26.statistics.api.controller.request.TransactionRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionControllerTest extends BaseTests {

    @Test
    public void postValidTransaction() throws Exception {

        TransactionRequest request = new TransactionRequest(12.3, Instant.now());

        super.mvc.perform(post(TransactionController.BASE_URL)
            .contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    public void postInvalidTransaction() throws Exception {

        Instant invalidTimestamp = Instant.now().minus(60, ChronoUnit.SECONDS);

        TransactionRequest request = new TransactionRequest(12.3, invalidTimestamp);

        super.mvc.perform(post(TransactionController.BASE_URL)
            .contentType(APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().isNoContent())
            .andDo(print());
    }
}
