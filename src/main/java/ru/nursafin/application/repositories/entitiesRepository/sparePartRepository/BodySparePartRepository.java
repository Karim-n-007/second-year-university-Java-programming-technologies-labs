package ru.nursafin.application.repositories.entitiesRepository.sparePartRepository;

import ru.nursafin.domainModel.entities.sparePart.body.Body;

import java.util.List;
import java.util.UUID;

public interface BodySparePartRepository {
    UUID save(Body body);

    Body findById(UUID id);

    List<Body> findAll();

    void deleteById(UUID id);
}