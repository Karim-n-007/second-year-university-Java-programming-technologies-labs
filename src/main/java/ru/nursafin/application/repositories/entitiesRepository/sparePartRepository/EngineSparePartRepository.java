package ru.nursafin.application.repositories.entitiesRepository.sparePartRepository;

import ru.nursafin.domainModel.entities.sparePart.engine.Engine;

import java.util.List;
import java.util.UUID;

public interface EngineSparePartRepository {
    UUID save(Engine engine);

    Engine findById(UUID id);

    List<Engine> findAll();

    void deleteById(UUID id);
}
