package ru.nursafin.rates;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RateRequestListener {
    private final RateStore store;

    @RabbitListener(queues = RabbitConfig.REQUEST_QUEUE)
    public RateResponseMessage onRequest(RateRequestMessage request) {
        String currency;
        if (request.getCurrency() != null) {
            currency = request.getCurrency().toUpperCase();
        } else {
            currency = "";
        }
        Optional<BigDecimal> rate = store.get(currency);

        if (rate.isEmpty()) {
            return new RateResponseMessage(currency, BigDecimal.ZERO, System.currentTimeMillis(), false);
        }

        return new RateResponseMessage(currency, rate.get(), System.currentTimeMillis(), true);
    }
}
