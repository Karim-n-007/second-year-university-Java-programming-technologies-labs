package ru.nursafin.application.repositories.entitiesRepository.sparePartRepository;

import ru.nursafin.domainModel.entities.sparePart.interior.Interior;

import java.util.List;
import java.util.UUID;

public interface InteriorSparePartRepository {
    UUID save(Interior gearbox);

    Interior findById(UUID id);

    List<Interior> findAll();

    void deleteById(UUID id);
}
