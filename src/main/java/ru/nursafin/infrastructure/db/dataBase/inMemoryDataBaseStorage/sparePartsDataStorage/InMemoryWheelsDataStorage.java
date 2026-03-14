package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage;

import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryWheelsDataStorage {
    public final HashMap<UUID, Wheels> wheels = new HashMap<>();
}
