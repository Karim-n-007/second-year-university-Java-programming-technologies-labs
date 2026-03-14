package ru.nursafin.domainModel.entities.sparePart.gearbox;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.sparePart.SparePart;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Gearbox implements SparePart {
    private final GearboxType type;

    private final UUID id;
    private final String name;
    private Money price;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public Gearbox(GearboxType type, UUID id, String name, Money price) {
        this.type = type;
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

    public GearboxType getGearboxType() {
        return type;
    }
}
