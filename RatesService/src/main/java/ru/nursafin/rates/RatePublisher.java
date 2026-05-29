package ru.nursafin.rates;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class RatePublisher {
    private final RabbitTemplate rabbitTemplate;
    private final RateStore store;

    private static final Map<String, BigDecimal> BASE_RATES = Map.of(
            "USD", new BigDecimal("72"),
            "EUR", new BigDecimal("90"),
            "CNY", new BigDecimal("10"));

    private static final double DRIFT = 0.03;

    @Scheduled(fixedDelayString = "${app.rates.publish-interval-ms}")
    public void publish() {
        for (String currency: List.copyOf(BASE_RATES.keySet())) {
            BigDecimal base = BASE_RATES.get(currency);
            double factor = 1 + (ThreadLocalRandom.current().nextDouble() * 2 - 1) * DRIFT;
            BigDecimal rate = base.multiply(BigDecimal.valueOf(factor)).setScale(4, RoundingMode.HALF_UP);

            store.put(currency, rate);

            RateUpdateMessage message = new RateUpdateMessage(currency, rate, System.currentTimeMillis());

            rabbitTemplate.convertAndSend(RabbitConfig.RATES_EXCHANGE, "rates.update", message);
        }
    }
}
