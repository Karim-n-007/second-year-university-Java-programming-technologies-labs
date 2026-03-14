package ru.nursafin.infrastructure.repositories.inMemoryUsersRepository.clientRepository;

import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.domainModel.users.client.Client;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.usersDataStorage.clientDataStorage.ClientDataStorage;

import java.util.*;

public class InMemoryClientRepository implements ClientRepository {
    private final ClientDataStorage dataStorage;

    public InMemoryClientRepository(ClientDataStorage clientDataStorage) {
        this.dataStorage = clientDataStorage;
    }

    @Override
    public UUID save(Client client) {
        dataStorage.get().put(client.getId(), client);

        return client.getId();
    }

    @Override
    public Client findById(UUID id) {
        Client client = dataStorage.get().get(id);
        if (client == null) {
            throw new EntityNotFoundException("Client with id " + id + " not found");
        }

        return client;
    }

    @Override
    public List<Client> findAll() {
        Collection<Client> clients = dataStorage.get().values();

        return new ArrayList<>(clients);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.get().remove(id);
    }
}
