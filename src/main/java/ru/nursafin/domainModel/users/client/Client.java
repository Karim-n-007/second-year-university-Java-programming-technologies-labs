package ru.nursafin.domainModel.users.client;

import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.time.LocalDate;
import java.util.UUID;

public class Client {
    private final UUID id;
    private final String name;
    private final LocalDate birthday;
    private final String number;
    private final String email;

    public Client(UUID id, String name, LocalDate birthday, String number, String email) {
        if (id == null || name == null || birthday == null || number == null || email == null) {
            throw new DomainValidationException("Some information about Client is null");
        }

        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.number = number;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }
}
