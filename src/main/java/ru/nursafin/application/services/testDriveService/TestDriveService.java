package ru.nursafin.application.services.testDriveService;

import ru.nursafin.application.filters.testDrive.TestDriveFilter;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.testDriveRepository.TestDriveCarRepository;
import ru.nursafin.application.repositories.entitiesRepository.testDriveRepository.TestDriveRequestRepository;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequest;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequestStatus;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TestDriveService {
    private final TestDriveRequestRepository testDriveRequestRepository;
    private final TestDriveCarRepository testDriveCarRepository;
    private final CarModelRepository carModelRepository;
    private final ClientRepository clientRepository;
    private final Clock clock;

    public TestDriveService(TestDriveRequestRepository testDriveRequestRepository,
                            TestDriveCarRepository testDriveCarRepository,
                            CarModelRepository carModelRepository,
                            ClientRepository clientRepository) {
        this(testDriveRequestRepository, testDriveCarRepository, carModelRepository, clientRepository,
                Clock.systemDefaultZone());
    }

    public TestDriveService(TestDriveRequestRepository testDriveRequestRepository,
                            TestDriveCarRepository testDriveCarRepository,
                            CarModelRepository carModelRepository,
                            ClientRepository clientRepository,
                            Clock clock) {
        this.testDriveRequestRepository = testDriveRequestRepository;
        this.testDriveCarRepository = testDriveCarRepository;
        this.carModelRepository = carModelRepository;
        this.clientRepository = clientRepository;
        this.clock = clock;
    }

    public UUID requestTestDrive(UUID clientId, UUID carModelId, LocalDateTime startDateTime) {
        requireNotNull(clientId, "client");
        requireNotNull(carModelId, "car model");
        requireNotNull(startDateTime, "test drive date and time");

        clientRepository.findById(clientId);
        carModelRepository.findById(carModelId);

        if (!testDriveCarRepository.contains(carModelId)) {
            throw new DomainValidationException("Car model " + carModelId + " is not available for test drive");
        }
        if (!startDateTime.isAfter(LocalDateTime.now(clock))) {
            throw new DomainValidationException("Test drive can be booked only for the future");
        }
        if (isTimeAlreadyTaken(carModelId, startDateTime)) {
            throw new DomainValidationException(
                    "Car model " + carModelId + " is already booked for " + startDateTime);
        }

        TestDriveRequest request = new TestDriveRequest(UUID.randomUUID(), clientId, carModelId, startDateTime);

        return testDriveRequestRepository.save(request);
    }

    public void addCarToTestDriveList(UUID carModelId) {
        requireNotNull(carModelId, "car model");
        carModelRepository.findById(carModelId);

        testDriveCarRepository.add(carModelId);
    }

    public void removeCarFromTestDriveList(UUID carModelId) {
        requireNotNull(carModelId, "car model");

        testDriveCarRepository.remove(carModelId);
    }

    public List<CarModel> getCarsAvailableForTestDrive() {
        return testDriveCarRepository.findAll().stream()
                .map(carModelRepository::findById)
                .sorted(Comparator.comparing(CarModel::getBrand).thenComparing(CarModel::getName))
                .toList();
    }

    public TestDriveRequest findById(UUID requestId) {
        return testDriveRequestRepository.findById(requestId);
    }

    public List<TestDriveRequest> findAll() {
        return testDriveRequestRepository.findAll();
    }

    public List<TestDriveRequest> findAll(TestDriveFilter filter) {
        return testDriveRequestRepository.findAll().stream()
                .filter(request -> filter.getClientId() == null ||
                        request.getClientId().equals(filter.getClientId()))
                .filter(request -> filter.getCarModelId() == null ||
                        request.getCarModelId().equals(filter.getCarModelId()))
                .filter(request -> filter.getStatus() == null ||
                        request.getStatus() == filter.getStatus())
                .sorted(Comparator.comparing(TestDriveRequest::getStartDateTime))
                .toList();
    }

    public void changeStatus(UUID requestId, TestDriveRequestStatus newStatus) {
        TestDriveRequest request = testDriveRequestRepository.findById(requestId);
        request.changeStatus(newStatus);

        testDriveRequestRepository.save(request);
    }

    public void deleteById(UUID requestId) {
        testDriveRequestRepository.findById(requestId);

        testDriveRequestRepository.deleteById(requestId);
    }

    private boolean isTimeAlreadyTaken(UUID carModelId, LocalDateTime startDateTime) {
        return testDriveRequestRepository.findAll().stream()
                .filter(request -> request.getStatus() != TestDriveRequestStatus.CANCELLED)
                .filter(request -> request.getCarModelId().equals(carModelId))
                .anyMatch(request -> request.getStartDateTime().equals(startDateTime));
    }

    private void requireNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new DomainValidationException("missing required field \"" + fieldName + "\"");
        }
    }
}
