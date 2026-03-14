package ru.nursafin.application.repositories.usersRepository.clientRepository;

import ru.nursafin.domainModel.users.client.Client;

import java.util.List;
import java.util.UUID;

public interface ClientRepository {
    UUID save(Client client);

    Client findById(UUID id);

    List<Client> findAll();

    void deleteById(UUID id);
}
