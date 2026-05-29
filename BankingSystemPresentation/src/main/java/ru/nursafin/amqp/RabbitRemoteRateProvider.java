package ru.nursafin.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import ru.nursafin.model.CurrencyRate;
import ru.nursafin.service.RemoteRateProvider;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RabbitRemoteRateProvider implements RemoteRateProvider {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.request-queue}")
    private String requestQueue;


    @Override
    public Optional<CurrencyRate> fetchRate(String currency) {
        RateResponseMessage response = rabbitTemplate.convertSendAndReceiveAsType("", requestQueue,
                new RateRequestMessage(currency), new ParameterizedTypeReference<RateResponseMessage>() {} );

        if (response == null || !response.isFound()) {
            return Optional.empty();
        }
        return Optional.of(new CurrencyRate(response.getCurrency(), response.getRateToRub(), Instant.ofEpochMilli(response.getTimestamp())));
    }
}
