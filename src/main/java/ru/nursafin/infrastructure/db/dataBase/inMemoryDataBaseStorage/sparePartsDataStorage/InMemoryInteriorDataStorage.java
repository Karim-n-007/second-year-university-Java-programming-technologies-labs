package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage;

import ru.nursafin.domainModel.entities.sparePart.interior.Interior;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryInteriorDataStorage {
    public final HashMap<UUID, Interior> interiors = new HashMap<>();
}
