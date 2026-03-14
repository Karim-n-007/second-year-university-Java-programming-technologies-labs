package ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemorySteeringWheelRepository;

import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.SteeringWheelSparePartRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemorySteeringWheelDataStorage;

import java.util.*;

public class InMemorySteeringWheelRepository implements SteeringWheelSparePartRepository {
    private final InMemorySteeringWheelDataStorage dataStorage;

    public InMemorySteeringWheelRepository(InMemorySteeringWheelDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(SteeringWheel steeringWheel) {
        dataStorage.steeringWheels.put(steeringWheel.getId(), steeringWheel);

        return steeringWheel.getId();
    }

    @Override
    public SteeringWheel findById(UUID id) {
        SteeringWheel steeringWheel = dataStorage.steeringWheels.get(id);
        if (steeringWheel == null) {
            throw new EntityNotFoundException("No such steering wheel with id " + id);
        }

        return steeringWheel;
    }

    @Override
    public List<SteeringWheel> findAll() {
        Collection<SteeringWheel> steeringWheels = dataStorage.steeringWheels.values();

        return new ArrayList<>(steeringWheels);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.steeringWheels.remove(id);
    }
}
