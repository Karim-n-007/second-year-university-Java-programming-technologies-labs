package ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryEngineRepository;

import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.EngineSparePartRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryEngineDataStorage;

import java.util.*;

public class InMemoryEngineRepository implements EngineSparePartRepository {
    private final InMemoryEngineDataStorage dataStorage;

    public InMemoryEngineRepository(InMemoryEngineDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(Engine engine) {
        dataStorage.engines.put(engine.getId(), engine);

        return engine.getId();
    }

    @Override
    public Engine findById(UUID id) {
        Engine engine = dataStorage.engines.get(id);
        if (engine == null) {
            throw new EntityNotFoundException("No such engine with id " + id);
        }

        return engine;
    }

    @Override
    public List<Engine> findAll() {
        Collection<Engine> engines = dataStorage.engines.values();

        return new ArrayList<>(engines);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.engines.remove(id);
    }
}
