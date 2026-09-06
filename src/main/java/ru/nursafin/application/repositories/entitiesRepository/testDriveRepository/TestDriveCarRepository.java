package ru.nursafin.application.repositories.entitiesRepository.testDriveRepository;

import java.util.Set;
import java.util.UUID;

public interface TestDriveCarRepository {
    void add(UUID carModelId);

    void remove(UUID carModelId);

    boolean contains(UUID carModelId);

    Set<UUID> findAll();
}
