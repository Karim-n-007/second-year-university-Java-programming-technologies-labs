package ru.nursafin.application.services.userService.clientService;

import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.domainModel.exceptions.DomainValidationException;
import ru.nursafin.domainModel.users.client.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public UUID createClient(String name, LocalDate birthday, String number, String email) {
        Client client = new Client(UUID.randomUUID(), name, birthday, number, email);

        return clientRepository.save(client);
    }

    public UUID updateClient(UUID clientId, String name, LocalDate birthday, String number, String email) {
        if (clientId == null) {
            throw new DomainValidationException("missing required field \"client\"");
        }
        clientRepository.findById(clientId);

        Client updatedClient = new Client(clientId, name, birthday, number, email);

        return clientRepository.save(updatedClient);
    }

    public Client findById(UUID clientId) {
        return clientRepository.findById(clientId);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public void deleteById(UUID clientId) {
        clientRepository.findById(clientId);

        clientRepository.deleteById(clientId);
    }
}
