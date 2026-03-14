package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.usersDataStorage.employeeDataStorage;

import ru.nursafin.domainModel.users.Employee.Employee;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryEmployeeDataStorage implements EmployeeDataStorage {
    private final HashMap<UUID, Employee> employees = new HashMap<>();

    @Override
    public HashMap<UUID, Employee> get() {
        return employees;
    }
}
