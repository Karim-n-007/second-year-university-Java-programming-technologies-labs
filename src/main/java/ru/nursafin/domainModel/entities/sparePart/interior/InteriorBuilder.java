package ru.nursafin.domainModel.entities.sparePart.interior;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InteriorBuilder {
    private InteriorType type = null;
    private String color = null;

    private UUID id = null;
    private String name = null;
    private Money price = null;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public InteriorBuilder withId(UUID id) {
        if (id == null) {
            throw new DomainValidationException("id is null");
        }
        this.id = id;

        return this;
    }

    public InteriorBuilder withName(String name) {
        if (name == null) {
            throw new DomainValidationException("name is null");
        }
        this.name = name;

        return this;
    }

    public InteriorBuilder withPrice(Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }
        this.price = price;

        return this;
    }

    public InteriorBuilder withCompatibleCarModelId(UUID carModelsId) {
        if (carModelsId == null) {
            throw new DomainValidationException("carModelsId is null");
        }
        this.compatibleCarModelsId.add(carModelsId);

        return this;
    }

    public InteriorBuilder withInteriorType(InteriorType interiorType) {
        if (interiorType == null) {
            throw new DomainValidationException("interiorType is null");
        }
        this.type = interiorType;

        return this;
    }

    public InteriorBuilder withColor(String color) {
        if (color == null) {
            throw new DomainValidationException("color is null");
        }
        this.color = color;

        return this;
    }

    public Interior build() {
        if (compatibleCarModelsId.isEmpty()) {
            throw new DomainValidationException("compatibleCarModelsId is null");
        } else if (id == null || name == null || price == null || type == null) {
            throw new DomainValidationException("some information is null");
        }

        return new Interior(type, color, id, name, price, compatibleCarModelsId);
    }
}
