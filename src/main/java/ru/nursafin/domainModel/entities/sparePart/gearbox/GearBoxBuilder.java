package ru.nursafin.domainModel.entities.sparePart.gearbox;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GearBoxBuilder {
    private GearboxType type;

    private UUID id = null;
    private String name = null;
    private Money price = null;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public GearBoxBuilder withId(UUID id) {
        if (id == null) {
            throw new DomainValidationException("id is null");
        }
        this.id = id;

        return this;
    }

    public GearBoxBuilder withName(String name) {
        if (name == null) {
            throw new DomainValidationException("name is null");
        }
        this.name = name;

        return this;
    }

    public GearBoxBuilder withPrice(Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }
        this.price = price;

        return this;
    }

    public GearBoxBuilder withCompatibleCarModelId(UUID carModelsId) {
        if (carModelsId == null) {
            throw new DomainValidationException("carModelsId is null");
        }
        this.compatibleCarModelsId.add(carModelsId);

        return this;
    }

    public GearBoxBuilder withType(GearboxType type) {
        if (type == null) {
            throw new DomainValidationException("type is null");
        }
        this.type = type;

        return this;
    }

    public Gearbox build() {
        if (compatibleCarModelsId.isEmpty()) {
            throw new DomainValidationException("compatibleCarModelsId is null");
        } else if (id == null || name == null || price == null || type == null) {
            throw new DomainValidationException("some information is null");
        }

        return new Gearbox(type, id, name, price, compatibleCarModelsId);
    }
}
