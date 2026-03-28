package ru.nursafin.dto;

import ru.nursafin.money.Money;

public record TransferReceipt(
        Long sourceAccountId,
        Long target,
        Money amount,
        Money commissionAmount,
        Money totalDebited,
        Money balanceAfter,
        Money targetBalanceAfter
) {
}
