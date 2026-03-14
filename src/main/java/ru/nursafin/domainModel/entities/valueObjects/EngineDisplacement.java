package ru.nursafin.domainModel.entities.valueObjects;

import ru.nursafin.domainModel.exceptions.DomainValidationException;

public record EngineDisplacement(int value) implements Comparable<EngineDisplacement>{
    public EngineDisplacement {
        if (value < 0) {
            throw new DomainValidationException("Engine displacement value cannot be negative");
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(EngineDisplacement other) {
        return Integer.compare(this.value, other.value);
    }
}
