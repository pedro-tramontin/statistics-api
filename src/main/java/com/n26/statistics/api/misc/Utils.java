package com.n26.statistics.api.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);

    public Instant now() {
        return Instant.now();
    }

    public long mapKey(Instant instant) {
        return instant.getEpochSecond();
    }

    public BigDecimal toBigDecimalScaledToTwo(Double value) {
        return BigDecimal.valueOf(value.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

}
