package ru.nursafin.domainModel.entities.sparePart.steeringWheel;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SteeringWheelBuilder {
    private SteeringWheelType type = null;
    private SteeringWheelMaterial material = null;

    private UUID id = null;
    private String name = null;
    private Money price = null;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public SteeringWheelBuilder withId(UUID id) {
        if (id == null) {
            throw new DomainValidationException("id is null");
        }
        this.id = id;

        return this;
    }

    public SteeringWheelBuilder withName(String name) {
        if (name == null) {
            throw new DomainValidationException("name is null");
        }
        this.name = name;

        return this;
    }

    public SteeringWheelBuilder withPrice(Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }
        this.price = price;

        return this;
    }

    public SteeringWheelBuilder withCompatibleCarModelId(UUID carModelsId) {
        if (carModelsId == null) {
            throw new DomainValidationException("carModelsId is null");
        }
        this.compatibleCarModelsId.add(carModelsId);

        return this;
    }

    public SteeringWheelBuilder withType(SteeringWheelType type) {
        if (type == null) {
            throw new DomainValidationException("type is null");
        }
        this.type = type;

        return this;
    }

    public SteeringWheelBuilder withMaterial(SteeringWheelMaterial material) {
        if (material == null) {
            throw new DomainValidationException("material is null");
        }
        this.material = material;

        return this;
    }

    public SteeringWheel build() {
        if (compatibleCarModelsId.isEmpty()) {
            throw new DomainValidationException("compatibleCarModelsId is null");
        } else if (id == null || name == null || price == null || type == null || material == null) {
            throw new DomainValidationException("some information is null");
        }

        return new SteeringWheel(type, material, id, name, price, compatibleCarModelsId);
    }
}
