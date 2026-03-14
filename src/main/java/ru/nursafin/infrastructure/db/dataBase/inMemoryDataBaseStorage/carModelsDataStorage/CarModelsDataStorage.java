package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage;

import ru.nursafin.domainModel.entities.car.CarModel;

import java.util.HashMap;
import java.util.UUID;

public interface CarModelsDataStorage {
    HashMap<UUID, CarModel> get();
}
