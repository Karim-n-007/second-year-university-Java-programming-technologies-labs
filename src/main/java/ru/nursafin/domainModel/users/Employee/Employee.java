package ru.nursafin.domainModel.users.Employee;

import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.UUID;

public class Employee {
    private final UUID id;
    private final String name;
    private final EmployeeRole role;

    public Employee(UUID id, String name, EmployeeRole role) {
        if (id == null || name == null || name.isBlank() || role == null) {
            throw new DomainValidationException("Some information about employee is null");
        }

        this.id = id;
        this.name = name;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public boolean hasRole(EmployeeRole expectedRole) {
        return role == expectedRole;
    }
}
