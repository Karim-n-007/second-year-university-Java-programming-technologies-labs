package ru.nursafin.application.filters.testDrive;

import ru.nursafin.domainModel.entities.testDrive.TestDriveRequestStatus;

import java.util.UUID;

public class TestDriveFilter {
    private UUID clientId;
    private UUID carModelId;
    private TestDriveRequestStatus status;

    public TestDriveFilter withClientId(UUID clientId) {
        this.clientId = clientId;
        return this;
    }

    public TestDriveFilter withCarModelId(UUID carModelId) {
        this.carModelId = carModelId;
        return this;
    }

    public TestDriveFilter withStatus(TestDriveRequestStatus status) {
        this.status = status;
        return this;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getCarModelId() {
        return carModelId;
    }

    public TestDriveRequestStatus getStatus() {
        return status;
    }
}
