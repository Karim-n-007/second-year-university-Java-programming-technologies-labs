package ru.nursafin.domainModel.entities.sparePart.body;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.sparePart.SparePart;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Body implements SparePart {
    private final BodyType bodyType;

    private final UUID id;
    private final String name;
    private Money price;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public Body(BodyType bodyType, UUID id, String name, Money price) {
        this(bodyType, id, name, price, Set.of());
    }

    public Body(BodyType bodyType, UUID id, String name, Money price, Set<UUID> compatibleCarModelsId) {
        this.bodyType = bodyType;
        this.id = id;
        this.name = name;
        this.price = price;
        this.compatibleCarModelsId.addAll(compatibleCarModelsId);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Money getPrice() {
        return price;
    }

    @Override
    public void setPrice(Money price) {
        this.price = price;
    }

    @Override
    public void addCompatibleCar(UUID id) {
        compatibleCarModelsId.add(id);
    }

    @Override
    public Set<UUID> getCompatibleCars() {
        return Set.copyOf(compatibleCarModelsId);
    }

    public BodyType getBodyType() {
        return bodyType;
    }
}
