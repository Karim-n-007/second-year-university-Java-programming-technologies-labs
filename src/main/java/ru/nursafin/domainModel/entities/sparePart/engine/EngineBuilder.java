package ru.nursafin.domainModel.entities.sparePart.engine;

import ru.nursafin.domainModel.entities.valueObjects.EngineDisplacement;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EngineBuilder {
    private Power power = null;
    private EngineDisplacement displacement = null;
    private FuelType fuelType = null;

    private UUID id = null;
    private String name = null;
    private Money price = null;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public EngineBuilder withId(UUID id) {
        if (id == null) {
            throw new DomainValidationException("id is null");
        }
        this.id = id;

        return this;
    }

    public EngineBuilder withName(String name) {
        if (name == null) {
            throw new DomainValidationException("name is null");
        }
        this.name = name;

        return this;
    }

    public EngineBuilder withPrice(Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }
        this.price = price;

        return this;
    }

    public EngineBuilder withCompatibleCarModelId(UUID carModelsId) {
        if (carModelsId == null) {
            throw new DomainValidationException("carModelsId is null");
        }
        this.compatibleCarModelsId.add(carModelsId);

        return this;
    }

    public EngineBuilder withPower(Power power) {
        if (power == null) {
            throw new DomainValidationException("power is null");
        }
        this.power = power;

        return this;
    }

    public EngineBuilder withDisplacement(EngineDisplacement displacement) {
        if (displacement == null) {
            throw new DomainValidationException("displacement is null");
        }
        this.displacement = displacement;

        return this;
    }

    public EngineBuilder withFuelType(FuelType fuelType) {
        if (fuelType == null) {
            throw new DomainValidationException("fuelType is null");
        }
        this.fuelType = fuelType;

        return this;
    }

    public Engine build() {
        if (compatibleCarModelsId.isEmpty()) {
            throw new DomainValidationException("compatibleCarModelsId is null");
        } else if (id == null || name == null || price == null ||
                displacement == null || fuelType == null || power == null) {
            throw new DomainValidationException("some information is null");
        }

        return new Engine(power, displacement, fuelType, id, name, price);
    }
}