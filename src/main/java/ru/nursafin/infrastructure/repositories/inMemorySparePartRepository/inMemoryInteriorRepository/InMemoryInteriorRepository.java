package ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryInteriorRepository;

import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.InteriorSparePartRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryInteriorDataStorage;

import java.util.*;

public class InMemoryInteriorRepository implements InteriorSparePartRepository {
    private final InMemoryInteriorDataStorage dataStorage;

    public InMemoryInteriorRepository(InMemoryInteriorDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(Interior interior) {
        dataStorage.interiors.put(interior.getId(), interior);

        return interior.getId();
    }

    @Override
    public Interior findById(UUID id) {
        Interior interior = dataStorage.interiors.get(id);
        if (interior == null) {
            throw new EntityNotFoundException("No such interior with id: " + id);
        }

        return interior;
    }

    @Override
    public List<Interior> findAll() {
        Collection<Interior> interiors = dataStorage.interiors.values();

        return new ArrayList<>(interiors);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.interiors.remove(id);
    }
}
