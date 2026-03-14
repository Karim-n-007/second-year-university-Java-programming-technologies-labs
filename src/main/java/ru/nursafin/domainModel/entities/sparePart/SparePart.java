package ru.nursafin.domainModel.entities.sparePart;

import ru.nursafin.domainModel.entities.valueObjects.Money;

import java.util.Set;
import java.util.UUID;

public interface SparePart {
    public UUID getId();

    public String getName();

    public Money getPrice();

    public void setPrice(Money price);

    public void addCompatibleCar(UUID id);

    public Set<UUID> getCompatibleCars();
}

