package ru.nursafin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nursafin.exception.ValidationException;
import ru.nursafin.money.Money;


import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Operation {
    private Long operationId;

    private Long accountId;

    private OperationType operationType;

    private Money amount;

    private Money commissionAmount;

    private Money balanceAfter;

    private LocalDateTime dateTime;

    private Long relatedAccountId;

    public Operation(Long operationId, Long accountId, OperationType operationType, Money amount, Money commissionAmount, Money balanceAfter, LocalDateTime dateTime, Long relatedAccountId) {
        if (accountId == null) {
            throw new ValidationException("Account id is null");
        }
        if (operationType == null) {
            throw new ValidationException("Operation type is null");
        }
        if (amount == null || commissionAmount == null || balanceAfter == null) {
            throw new ValidationException("Amount and commission amount are null");
        }
        if (dateTime == null) {
            throw new ValidationException("Date is null");
        }

        this.accountId = accountId;
        this.operationType = operationType;
        this.amount = amount;
        this.commissionAmount = commissionAmount;
        this.balanceAfter = balanceAfter;
        this.relatedAccountId = relatedAccountId;
        this.dateTime = dateTime;
        this.operationId = operationId;
    }

    public Operation(Long accountId, OperationType operationType, Money amount, Money commissionAmount, Money balanceAfter, Long relatedAccountId) {
        this(null, accountId, operationType, amount, commissionAmount, balanceAfter, LocalDateTime.now(), relatedAccountId);
    }
}
