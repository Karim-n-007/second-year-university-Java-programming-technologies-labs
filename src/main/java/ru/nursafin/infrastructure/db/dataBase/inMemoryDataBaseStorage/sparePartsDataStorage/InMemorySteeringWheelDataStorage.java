package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage;

import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;

import java.util.HashMap;
import java.util.UUID;

public class InMemorySteeringWheelDataStorage {
    public final HashMap<UUID, SteeringWheel> steeringWheels = new HashMap<>();
}
