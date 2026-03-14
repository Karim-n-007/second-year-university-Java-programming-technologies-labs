package ru.nursafin.infrastructure.repositories.inMemoryUsersRepository.employeeRepository;

import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.usersDataStorage.employeeDataStorage.EmployeeDataStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class InMemoryEmployeeRepository implements EmployeeRepository {
    private final EmployeeDataStorage dataStorage;

    public InMemoryEmployeeRepository(EmployeeDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(Employee employee) {
        dataStorage.get().put(employee.getId(), employee);

        return employee.getId();
    }

    @Override
    public Employee findById(UUID id) {
        Employee employee = dataStorage.get().get(id);
        if (employee == null) {
            throw new EntityNotFoundException("Employee with id " + id + " not found");
        }

        return employee;
    }

    @Override
    public List<Employee> findAll() {
        Collection<Employee> employees = dataStorage.get().values();

        return new ArrayList<>(employees);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.get().remove(id);
    }
}
