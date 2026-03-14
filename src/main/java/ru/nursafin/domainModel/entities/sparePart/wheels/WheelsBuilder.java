package ru.nursafin.domainModel.entities.sparePart.wheels;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WheelsBuilder {
    private WheelSeason season;

    private UUID id = null;
    private String name = null;
    private Money price = null;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public WheelsBuilder withId(UUID id) {
        if (id == null) {
            throw new DomainValidationException("id is null");
        }
        this.id = id;

        return this;
    }

    public WheelsBuilder withName(String name) {
        if (name == null) {
            throw new DomainValidationException("name is null");
        }
        this.name = name;

        return this;
    }

    public WheelsBuilder withPrice(Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }
        this.price = price;

        return this;
    }

    public WheelsBuilder withCompatibleCarModelId(UUID carModelsId) {
        if (carModelsId == null) {
            throw new DomainValidationException("carModelsId is null");
        }
        this.compatibleCarModelsId.add(carModelsId);

        return this;
    }

    public WheelsBuilder withSeason(WheelSeason season) {
        if (season == null) {
            throw new DomainValidationException("season is null");
        }
        this.season = season;

        return this;
    }

    public Wheels build() {
        if (compatibleCarModelsId.isEmpty()) {
            throw new DomainValidationException("compatibleCarModelsId is null");
        } else if (id == null || name == null || price == null || season == null) {
            throw new DomainValidationException("some information is null");
        }

        return new Wheels(season, id, name, price);
    }
}
