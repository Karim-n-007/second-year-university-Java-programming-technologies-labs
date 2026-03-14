package ru.nursafin.domainModel.entities.valueObjects;

import ru.nursafin.domainModel.exceptions.DomainValidationException;

public record Money(double value) implements Comparable<Money>{
    public Money {
        if (value < 0) {
            throw new DomainValidationException("Money value cannot be negative");
        }
    }

    public Money plus(Money other) {
        return new Money(this.value + other.value);
    }

    @Override
    public int compareTo(Money other) {
        return Double.compare(this.value, other.value);
    }
}