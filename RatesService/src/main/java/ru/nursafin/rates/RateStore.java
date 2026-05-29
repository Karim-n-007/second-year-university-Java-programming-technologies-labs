package ru.nursafin.rates;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateStore {
    private final Map<String, BigDecimal> rates = new ConcurrentHashMap<>();

    public void put(String currency, BigDecimal rate) {
        rates.put(currency, rate);
    }

    public Optional<BigDecimal> get(String currency) {
        if (currency == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(rates.get(currency.toUpperCase()));
    }
}
