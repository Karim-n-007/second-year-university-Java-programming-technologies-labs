package ru.nursafin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nursafin.exception.RateUnavailableException;
import ru.nursafin.model.CurrencyRate;
import ru.nursafin.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
public class RateService {
    private final Map<String, CurrencyRate> cache = new ConcurrentHashMap<>();

    private static final Duration FRESHNESS_TTL = Duration.ofSeconds(120);

    private final RemoteRateProvider remoteRateProvider;

    public void updateRate(CurrencyRate rate) {
        cache.put(rate.getCurrency(), rate);
    }

    public Money convertFromRub(Money rubAmount, String currency) {
        String code = currency.toUpperCase();
        CurrencyRate rate = getFreshRate(code);
        BigDecimal converted = rubAmount.getAmount().divide(rate.getRateToRub(),4, RoundingMode.HALF_UP);

        return new Money(converted);
    }

    private CurrencyRate getFreshRate(String code) {
        CurrencyRate cached = cache.get(code);
        if (cached != null && cached.isFresh(FRESHNESS_TTL)) {
            return cached;
        }

        Optional<CurrencyRate> fetched = remoteRateProvider.fetchRate(code);
        if (fetched.isEmpty()) {
            throw new RateUnavailableException(code);
        }
        CurrencyRate fresh = fetched.get();
        cache.put(code, fresh);

        return fresh;
    }
}
