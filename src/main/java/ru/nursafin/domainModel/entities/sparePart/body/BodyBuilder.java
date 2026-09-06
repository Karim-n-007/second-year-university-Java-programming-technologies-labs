package ru.nursafin.domainModel.entities.sparePart.body;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BodyBuilder {
    private BodyType bodyType = null;

    private UUID id = null;
    private String name = null;
    private Money price = null;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public BodyBuilder withId(UUID id) {
        if (id == null) {
            throw new DomainValidationException("id is null");
        }
        this.id = id;

        return this;
    }

    public BodyBuilder withName(String name) {
        if (name == null) {
            throw new DomainValidationException("name is null");
        }
        this.name = name;

        return this;
    }

    public BodyBuilder withPrice(Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }
        this.price = price;

        return this;
    }

    public BodyBuilder withCompatibleCarModelId(UUID carModelsId) {
        if (carModelsId == null) {
            throw new DomainValidationException("carModelsId is null");
        }
        this.compatibleCarModelsId.add(carModelsId);

        return this;
    }

    public BodyBuilder withBody(BodyType body) {
        if (body == null) {
            throw new DomainValidationException("body is null");
        }
        this.bodyType = body;

        return this;
    }


    public Body build() {
        if (compatibleCarModelsId.isEmpty()) {
            throw new DomainValidationException("compatibleCarModelsId is empty");
        } else if (id == null || name == null || price == null || bodyType == null) {
            throw new DomainValidationException("some information is null");
        }

        return new Body(bodyType, id, name, price, compatibleCarModelsId);
    }
}
