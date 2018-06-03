package com.n26.statistics.api.controller.request;

import com.n26.statistics.api.controller.validator.annotation.NotTooOld;

import java.time.Instant;
import lombok.Data;

@Data
public class TransactionRequest {

    private final Double amount;

    @NotTooOld
    private final Instant timestamp;

}
