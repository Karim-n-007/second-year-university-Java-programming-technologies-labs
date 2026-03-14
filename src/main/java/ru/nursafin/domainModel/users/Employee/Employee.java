package ru.nursafin.domainModel.users.Employee;

import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.UUID;

public class Employee {
    private final UUID id;
    private final String name;

    public Employee(UUID id, String name) {
        if (id == null || name == null) {
            throw new DomainValidationException("Some information about employee is null");
        }

        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
