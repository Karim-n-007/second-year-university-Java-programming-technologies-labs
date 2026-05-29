package ru.nursafin.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.nursafin.model.CurrencyRate;
import ru.nursafin.service.RateService;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RateUpdateListener {
    private final RateService rateService;

    @RabbitListener(queues = "${app.rabbit.rates-queue}")
    public void onRateUpdate(RateUpdateMessage message) {
        rateService.updateRate(new CurrencyRate(message.getCurrency(), message.getRateToRub(), Instant.ofEpochMilli(message.getTimestamp())));
    }
}
