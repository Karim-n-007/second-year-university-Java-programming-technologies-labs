package ru.nursafin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceInCurrencyResponse {
    private Long accountId;
    private String currency;
    private BigDecimal balance;
}
