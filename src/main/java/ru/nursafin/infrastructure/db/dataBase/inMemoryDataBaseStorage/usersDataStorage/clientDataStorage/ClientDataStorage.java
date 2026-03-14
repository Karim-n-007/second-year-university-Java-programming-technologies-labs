package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.usersDataStorage.clientDataStorage;

import ru.nursafin.domainModel.users.client.Client;

import java.util.HashMap;
import java.util.UUID;

public interface ClientDataStorage {
    HashMap<UUID, Client> get();
}
