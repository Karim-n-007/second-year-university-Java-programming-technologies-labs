package ru.nursafin.rates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateResponseMessage {
    private String currency;
    private BigDecimal rateToRub;
    private long timestamp;
    private boolean found;
}
