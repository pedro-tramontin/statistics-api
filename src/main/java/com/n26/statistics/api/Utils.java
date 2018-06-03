package com.n26.statistics.api;

import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    public Instant now() {
        return Instant.now();
    }

    public long mapKey(Instant instant) {
        return instant.getEpochSecond();
    }

}
