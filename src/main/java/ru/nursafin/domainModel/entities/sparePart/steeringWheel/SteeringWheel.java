package ru.nursafin.domainModel.entities.sparePart.steeringWheel;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.sparePart.SparePart;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SteeringWheel implements SparePart {
    private final SteeringWheelType type;
    private final SteeringWheelMaterial material;

    private final UUID id;
    private final String name;
    private Money price;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public SteeringWheel(SteeringWheelType type, SteeringWheelMaterial material, UUID id, String name, Money price) {
        this.type = type;
        this.material = material;
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public SteeringWheelMaterial getMaterial() {
        return material;
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

    public SteeringWheelType getType() {
        return type;
    }
}
