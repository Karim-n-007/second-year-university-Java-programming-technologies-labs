package ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryWheelsRepository;

import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.WheelsSparePartRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryWheelsDataStorage;

import java.util.*;

public class InMemoryWheelsRepository implements WheelsSparePartRepository {
    private final InMemoryWheelsDataStorage dataStorage;

    public InMemoryWheelsRepository(InMemoryWheelsDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(Wheels wheels) {
        dataStorage.wheels.put(wheels.getId(), wheels);

        return wheels.getId();
    }

    @Override
    public Wheels findById(UUID id) {
        Wheels wheels = dataStorage.wheels.get(id);
        if (wheels == null) {
            throw new EntityNotFoundException("No such wheels with id " + id);
        }

        return wheels;
    }

    @Override
    public List<Wheels> findAll() {
        Collection<Wheels> wheels = dataStorage.wheels.values();

        return new ArrayList<>(wheels);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.wheels.remove(id);
    }
}
