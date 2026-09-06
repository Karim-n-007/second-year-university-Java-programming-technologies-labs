package ru.nursafin.infrastructure.repositories.inMemoryTestDriveRepository;

import ru.nursafin.application.repositories.entitiesRepository.testDriveRepository.TestDriveRequestRepository;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequest;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.testDriveDataStorage.InMemoryTestDriveRequestDataStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class InMemoryTestDriveRequestRepository implements TestDriveRequestRepository {
    private final InMemoryTestDriveRequestDataStorage dataStorage;

    public InMemoryTestDriveRequestRepository(InMemoryTestDriveRequestDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(TestDriveRequest testDriveRequest) {
        UUID requestId = testDriveRequest.getId();
        dataStorage.requests.put(requestId, testDriveRequest);

        return requestId;
    }

    @Override
    public TestDriveRequest findById(UUID id) {
        TestDriveRequest testDriveRequest = dataStorage.requests.get(id);
        if (testDriveRequest == null) {
            throw new EntityNotFoundException("Test drive request with id " + id + " not found");
        }

        return testDriveRequest;
    }

    @Override
    public List<TestDriveRequest> findAll() {
        Collection<TestDriveRequest> requests = dataStorage.requests.values();

        return new ArrayList<>(requests);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.requests.remove(id);
    }
}
