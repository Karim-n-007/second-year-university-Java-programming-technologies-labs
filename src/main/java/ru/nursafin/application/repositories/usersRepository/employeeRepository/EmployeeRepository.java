package ru.nursafin.application.repositories.usersRepository.employeeRepository;

import ru.nursafin.domainModel.users.Employee.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository {
    UUID save(Employee employee);

    Employee findById(UUID id);

    List<Employee> findAll();

    void deleteById(UUID id);
}
