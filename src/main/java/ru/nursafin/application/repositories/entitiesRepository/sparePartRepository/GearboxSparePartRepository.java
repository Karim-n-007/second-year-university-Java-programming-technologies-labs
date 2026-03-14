package ru.nursafin.application.repositories.entitiesRepository.sparePartRepository;

import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;

import java.util.List;
import java.util.UUID;

public interface GearboxSparePartRepository {
    UUID save(Gearbox gearbox);

    Gearbox findById(UUID id);

    List<Gearbox> findAll();

    void deleteById(UUID id);
}
