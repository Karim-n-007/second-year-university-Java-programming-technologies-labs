package ru.nursafin.application.repositories.entitiesRepository.sparePartRepository;

import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;

import java.util.List;
import java.util.UUID;

public interface SteeringWheelSparePartRepository {
    UUID save(SteeringWheel steeringWheel);

    SteeringWheel findById(UUID id);

    List<SteeringWheel> findAll();

    void deleteById(UUID id);
}
