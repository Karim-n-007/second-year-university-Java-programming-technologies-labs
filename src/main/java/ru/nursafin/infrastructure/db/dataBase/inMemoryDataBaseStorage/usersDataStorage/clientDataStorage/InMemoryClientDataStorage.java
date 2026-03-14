package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.usersDataStorage.clientDataStorage;

import ru.nursafin.domainModel.users.client.Client;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryClientDataStorage implements ClientDataStorage {
    private final HashMap<UUID, Client> clients = new HashMap<>();

    @Override
    public HashMap<UUID, Client> get() {
        return clients;
    }
}
