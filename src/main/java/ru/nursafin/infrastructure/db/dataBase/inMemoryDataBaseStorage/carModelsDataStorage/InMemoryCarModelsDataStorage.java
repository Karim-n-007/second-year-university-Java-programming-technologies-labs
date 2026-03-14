package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage;

import ru.nursafin.domainModel.entities.car.CarModel;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryCarModelsDataStorage implements CarModelsDataStorage {
    private final HashMap<UUID, CarModel> carModels = new HashMap<>();

    @Override
    public HashMap<UUID, CarModel> get() {
        return carModels;
    }
}
