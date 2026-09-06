package ru.nursafin.domainModel.entities.testDrive;

import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestDriveRequest {
    private TestDriveRequestStatus status = TestDriveRequestStatus.REQUESTED;

    private final UUID id;
    private final UUID clientId;
    private final UUID carModelId;
    private final LocalDateTime startDateTime;

    public TestDriveRequest(UUID id, UUID clientId, UUID carModelId, LocalDateTime startDateTime) {
        if (id == null || clientId == null || carModelId == null || startDateTime == null) {
            throw new DomainValidationException("Some information about test drive request is null");
        }

        this.id = id;
        this.clientId = clientId;
        this.carModelId = carModelId;
        this.startDateTime = startDateTime;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getCarModelId() {
        return carModelId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public TestDriveRequestStatus getStatus() {
        return status;
    }

    public void changeStatus(TestDriveRequestStatus newStatus) {
        if (newStatus == null) {
            throw new DomainValidationException("Test drive request status is null");
        }
        if (!status.canChangeTo(newStatus)) {
            throw new DomainValidationException(
                    "Test drive request " + id + " cannot change status from " + status + " to " + newStatus);
        }

        this.status = newStatus;
    }
}
