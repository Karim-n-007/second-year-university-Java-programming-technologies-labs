package ru.nursafin.service;

import ru.nursafin.model.BankUser;
import ru.nursafin.money.Money;

import java.math.BigDecimal;

public class TransferCommissionPolicy {
    private static final BigDecimal OWN_RATE = new BigDecimal("0");
    private static final BigDecimal FRIEND_RATE = new BigDecimal("0.03");
    private static final BigDecimal DEFAULT_RATE = new BigDecimal("0.10");

    public BigDecimal getRateFor(BankUser sender, BankUser receiver) {
        if (isSameUser(sender, receiver)) {
            return OWN_RATE;
        }
        else if (sender.isFriendWith(receiver)) {
            return FRIEND_RATE;
        }

        return DEFAULT_RATE;
    }

    public Money calculateCommission(BankUser sender, BankUser receiver, BigDecimal amount) {
        BigDecimal rate = getRateFor(sender, receiver);

        BigDecimal totalCommission = amount.multiply(rate);

        return new Money(totalCommission);
    }

    private boolean isSameUser(BankUser sender, BankUser receiver) {
        if (sender == receiver) {
            return true;
        }

        return sender.getUserId() != null && receiver.getUserId() != null &&
             sender.getUserId().equals(receiver.getUserId());
    }
}
