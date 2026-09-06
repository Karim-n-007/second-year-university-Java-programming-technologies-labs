package ru.nursafin.domainModel.entities.valueObjects;

import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal value) implements Comparable<Money> {
    public static final int SCALE = 2;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        if (value == null) {
            throw new DomainValidationException("Money value is null");
        }

        value = value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public Money(long value) {
        this(BigDecimal.valueOf(value));
    }

    public Money(String value) {
        this(parse(value));
    }

    public Money plus(Money other) {
        if (other == null) {
            throw new DomainValidationException("Money value is null");
        }

        return new Money(this.value.add(other.value));
    }

    public Money minus(Money other) {
        if (other == null) {
            throw new DomainValidationException("Money value is null");
        }

        return new Money(this.value.subtract(other.value));
    }

    public boolean isNegative() {
        return value.signum() < 0;
    }

    @Override
    public int compareTo(Money other) {
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }

    private static BigDecimal parse(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Money value is null");
        }

        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException exception) {
            throw new DomainValidationException("Money value is not a number: " + value);
        }
    }
}
