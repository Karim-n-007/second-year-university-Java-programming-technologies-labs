package ru.nursafin.dto.response;

import ru.nursafin.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperationInCurrencyResponse(
        Long id,
        Long accountId,
        OperationType operationType,
        BigDecimal amount,
        BigDecimal commissionAmount,
        BigDecimal balanceAfterOperation,
        Long relatedAccountId,
        LocalDateTime createdAt,
        String currency
) {
}