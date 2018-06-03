package com.n26.statistics.api.controller.request;

import com.n26.statistics.api.controller.validator.annotation.NotTooOld;

import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import lombok.Data;

@Data
public class TransactionRequest {

    @ApiModelProperty(example = "12.3")
    private final Double amount;

    @ApiModelProperty(dataType = "java.lang.Long", example = "1478192204000")
    @NotTooOld
    private final Instant timestamp;

}
