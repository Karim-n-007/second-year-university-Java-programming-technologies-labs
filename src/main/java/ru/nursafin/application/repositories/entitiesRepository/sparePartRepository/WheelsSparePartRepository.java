package ru.nursafin.application.repositories.entitiesRepository.sparePartRepository;

import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;

import java.util.List;
import java.util.UUID;

public interface WheelsSparePartRepository {
    UUID save(Wheels wheels);

    Wheels findById(UUID id);

    List<Wheels> findAll();

    void deleteById(UUID id);
}
