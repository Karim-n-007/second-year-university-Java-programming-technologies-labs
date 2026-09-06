package ru.nursafin.domainModel.entities.sparePart.engine;

import ru.nursafin.domainModel.entities.valueObjects.EngineDisplacement;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;
import ru.nursafin.domainModel.entities.sparePart.SparePart;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Engine implements SparePart {
    private final Power power;
    private final EngineDisplacement engineDisplacement;
    private final FuelType fuelType;

    private final UUID id;
    private final String name;
    private Money price;
    private final Set<UUID> compatibleCarModelsId = new HashSet<>();

    public Engine(Power power, EngineDisplacement engineDisplacement, FuelType fuelType, UUID id, String name, Money price) {
        this(power, engineDisplacement, fuelType, id, name, price, Set.of());
    }

    public Engine(Power power, EngineDisplacement engineDisplacement, FuelType fuelType, UUID id, String name, Money price, Set<UUID> compatibleCarModelsId) {
        this.power = power;
        this.engineDisplacement = engineDisplacement;
        this.fuelType = fuelType;
        this.id = id;
        this.name = name;
        this.price = price;
        this.compatibleCarModelsId.addAll(compatibleCarModelsId);
    }


    public Power getPower() {
        return power;
    }

    public EngineDisplacement getEngineDisplacement() {
        return engineDisplacement;
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

    public FuelType getFuelType() {
        return fuelType;
    }
}
