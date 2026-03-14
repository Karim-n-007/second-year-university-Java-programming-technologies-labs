package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage;

import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryGearboxDataStorage {
    public final HashMap<UUID, Gearbox> gearboxes = new HashMap<>();
}
