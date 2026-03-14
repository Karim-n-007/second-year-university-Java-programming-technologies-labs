package ru.nursafin.domainModel.entities.sparePart.wheels;

import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.sparePart.SparePart;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Wheels implements SparePart {
    private final WheelSeason season;

    private final UUID id;
    private final String name;
    private Money price;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public Wheels(WheelSeason season, UUID id, String name, Money price) {
        this.season = season;
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

    public WheelSeason getSeason() {
        return season;
    }
}
