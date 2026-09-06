package ru.nursafin.infrastructure.repositories.inMemoryTestDriveRepository;

import ru.nursafin.application.repositories.entitiesRepository.testDriveRepository.TestDriveCarRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.testDriveDataStorage.InMemoryTestDriveCarDataStorage;

import java.util.Set;
import java.util.UUID;

public class InMemoryTestDriveCarRepository implements TestDriveCarRepository {
    private final InMemoryTestDriveCarDataStorage dataStorage;

    public InMemoryTestDriveCarRepository(InMemoryTestDriveCarDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public void add(UUID carModelId) {
        dataStorage.carModelsId.add(carModelId);
    }

    @Override
    public void remove(UUID carModelId) {
        dataStorage.carModelsId.remove(carModelId);
    }

    @Override
    public boolean contains(UUID carModelId) {
        return dataStorage.carModelsId.contains(carModelId);
    }

    @Override
    public Set<UUID> findAll() {
        return Set.copyOf(dataStorage.carModelsId);
    }
}
