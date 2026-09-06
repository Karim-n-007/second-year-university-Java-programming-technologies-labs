package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.testDriveDataStorage;

import ru.nursafin.domainModel.entities.testDrive.TestDriveRequest;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryTestDriveRequestDataStorage {
    public final HashMap<UUID, TestDriveRequest> requests = new HashMap<>();
}
