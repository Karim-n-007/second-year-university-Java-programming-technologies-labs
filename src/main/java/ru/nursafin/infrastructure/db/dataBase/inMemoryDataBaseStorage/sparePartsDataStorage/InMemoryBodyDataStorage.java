package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage;

import ru.nursafin.domainModel.entities.sparePart.body.Body;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryBodyDataStorage {
    public final HashMap<UUID, Body> bodies = new HashMap<>();
}
