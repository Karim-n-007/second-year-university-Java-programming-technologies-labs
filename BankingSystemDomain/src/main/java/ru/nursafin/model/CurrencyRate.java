package ru.nursafin.model;

import lombok.Getter;
import ru.nursafin.exception.ValidationException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Getter
public class CurrencyRate {
    private final String currency;
    private final BigDecimal rateToRub;
    private final Instant timestamp;


    public CurrencyRate(String currency, BigDecimal rateToRub, Instant timestamp) {
        if (currency == null || currency.length() != 3) {
            throw new ValidationException("currency must be a 3-letter code");
        }
        if (rateToRub == null || rateToRub.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("rateToRub must be > 0");
        }
        if (timestamp == null) {
            throw new ValidationException("timestamp must not be null");
        }
        this.currency = currency.toUpperCase();
        this.rateToRub = rateToRub;
        this.timestamp = timestamp;
    }

    public boolean isFresh(Duration ttl) {
        return timestamp.isAfter(Instant.now().minus(ttl));
    }
}
