package ru.nursafin.domainModel.entities.sparePart.interior;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.sparePart.SparePart;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Interior implements SparePart {
    private final InteriorType interiorType;
    private final String color;

    private final UUID id;
    private final String name;
    private Money price;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public Interior(InteriorType interiorType, String color, UUID id, String name, Money price) {
        this.interiorType = interiorType;
        this.color = color;
        this.id = id;
        this.name = name;
        this.price = price;
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
        return compatibleCarModelsId;
    }

    public InteriorType getInteriorType() {
        return interiorType;
    }

    public String getColor() {
        return color;
    }
}
