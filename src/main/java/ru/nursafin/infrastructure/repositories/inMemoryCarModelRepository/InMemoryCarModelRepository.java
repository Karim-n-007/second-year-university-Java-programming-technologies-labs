package ru.nursafin.infrastructure.repositories.inMemoryCarModelRepository;

import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage.InMemoryCarModelsDataStorage;

import java.util.*;

public class InMemoryCarModelRepository implements CarModelRepository {
    private final InMemoryCarModelsDataStorage dataStorage;

    public InMemoryCarModelRepository(InMemoryCarModelsDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(CarModel carModel) {
        if (carModel.getId() != null) {
            UUID carModelId = carModel.getId();
            dataStorage.get().put(carModelId, carModel);

            return carModelId;
        }

        UUID carModelId = madeCorrectId(dataStorage.get());
        dataStorage.get().put(carModelId, carModel);

        return carModelId;
    }

    @Override
    public CarModel findById(UUID id) {
        CarModel carModel = dataStorage.get().get(id);
        if (carModel == null) {
            throw new EntityNotFoundException("CarModel with id " + id + " not found");
        }

        return carModel;
    }

    @Override
    public List<CarModel> findAll() {
        Collection<CarModel> carModels = dataStorage.get().values();

        return new ArrayList<>(carModels);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.get().remove(id);
    }

    private UUID madeCorrectId(HashMap<UUID, CarModel> CarModelMap) {
        while (true) {
            UUID ModelCarId = UUID.randomUUID();

            if (!CarModelMap.containsKey(ModelCarId)) {
                return ModelCarId;
            }
        }
    }
}
