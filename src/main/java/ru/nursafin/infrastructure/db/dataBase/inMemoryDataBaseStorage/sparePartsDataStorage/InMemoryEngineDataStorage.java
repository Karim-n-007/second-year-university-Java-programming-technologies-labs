package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage;

import ru.nursafin.domainModel.entities.sparePart.engine.Engine;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryEngineDataStorage {
    public final HashMap<UUID, Engine> engines = new HashMap<>();
}
