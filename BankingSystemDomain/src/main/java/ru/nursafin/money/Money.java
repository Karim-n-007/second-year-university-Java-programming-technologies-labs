package ru.nursafin.money;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import ru.nursafin.exception.ValidationException;

import java.math.BigDecimal;

@Getter
@Embeddable
public class Money {
    private BigDecimal amount;

    public Money() {
        this.amount = BigDecimal.ZERO;
    }

    public Money(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Money must be >= 0");
        }
        this.amount = amount;
    }

    public Money increase(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money decrease(Money other) {
        return new Money(amount.subtract(other.amount));
    }
}
