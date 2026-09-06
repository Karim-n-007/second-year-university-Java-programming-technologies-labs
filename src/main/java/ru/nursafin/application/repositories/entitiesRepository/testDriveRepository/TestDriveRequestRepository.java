package ru.nursafin.application.repositories.entitiesRepository.testDriveRepository;

import ru.nursafin.domainModel.entities.testDrive.TestDriveRequest;

import java.util.List;
import java.util.UUID;

public interface TestDriveRequestRepository {
    UUID save(TestDriveRequest testDriveRequest);

    TestDriveRequest findById(UUID id);

    List<TestDriveRequest> findAll();

    void deleteById(UUID id);
}
