package ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryBodyRepository;

import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.BodySparePartRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryBodyDataStorage;

import java.util.*;

public class InMemoryBodyRepository implements BodySparePartRepository {
    private final InMemoryBodyDataStorage dataStorage;

    public InMemoryBodyRepository(InMemoryBodyDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(Body body) {
        dataStorage.bodies.put(body.getId(), body);

        return body.getId();
    }

    @Override
    public Body findById(UUID id) {
        Body body = dataStorage.bodies.get(id);
        if (body == null) {
            throw new EntityNotFoundException("No such body with id " + id);
        }

        return body;
    }

    @Override
    public List<Body> findAll() {
        Collection<Body> bodyCollection = dataStorage.bodies.values();

        return new ArrayList<>(bodyCollection);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.bodies.remove(id);
    }
}
