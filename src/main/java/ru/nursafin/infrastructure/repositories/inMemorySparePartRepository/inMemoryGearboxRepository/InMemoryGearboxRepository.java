package ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryGearboxRepository;

import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.GearboxSparePartRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryGearboxDataStorage;

import java.util.*;

public class InMemoryGearboxRepository implements GearboxSparePartRepository {
    private final InMemoryGearboxDataStorage dataStorage;

    public InMemoryGearboxRepository(InMemoryGearboxDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(Gearbox gearbox) {
        dataStorage.gearboxes.put(gearbox.getId(), gearbox);

        return gearbox.getId();
    }

    @Override
    public Gearbox findById(UUID id) {
        Gearbox gearbox = dataStorage.gearboxes.get(id);
        if (gearbox == null) {
            throw new EntityNotFoundException("No such gearbox with id " + id);
        }

        return gearbox;
    }

    @Override
    public List<Gearbox> findAll() {
        Collection<Gearbox> gearboxes = dataStorage.gearboxes.values();

        return new ArrayList<>(gearboxes);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.gearboxes.remove(id);
    }
}