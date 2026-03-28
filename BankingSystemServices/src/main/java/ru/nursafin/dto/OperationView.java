package ru.nursafin.dto;

import ru.nursafin.model.OperationType;
import ru.nursafin.money.Money;

import java.time.LocalDate;


public record OperationView (
    Long id,
    OperationType operationType,
    Money amount,
    Money commissionAmount,
    Money balanceAfter,
    Long relatedAccountId,
    LocalDate createdAt
) {}
