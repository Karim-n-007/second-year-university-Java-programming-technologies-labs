package ru.nursafin.amqp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateUpdateMessage {
    private String currency;
    private BigDecimal rateToRub;
    private long timestamp;

}
