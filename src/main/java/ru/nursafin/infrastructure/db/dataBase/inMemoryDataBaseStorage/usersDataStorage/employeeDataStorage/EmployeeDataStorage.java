package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.usersDataStorage.employeeDataStorage;

import ru.nursafin.domainModel.users.Employee.Employee;

import java.util.HashMap;
import java.util.UUID;

public interface EmployeeDataStorage {
    HashMap<UUID, Employee> get();
}
