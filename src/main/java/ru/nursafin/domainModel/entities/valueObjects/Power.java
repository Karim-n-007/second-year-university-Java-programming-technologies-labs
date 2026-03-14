package ru.nursafin.domainModel.entities.valueObjects;

import ru.nursafin.domainModel.exceptions.DomainValidationException;

public record Power(int value) implements Comparable<Power> {
    public Power {
        if (value < 0) {
            throw new DomainValidationException("Power value cannot be negative");
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Power other) {
        return Integer.compare(this.value, other.value);
    }
}
