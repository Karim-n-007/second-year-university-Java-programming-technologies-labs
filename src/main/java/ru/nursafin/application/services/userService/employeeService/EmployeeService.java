package ru.nursafin.application.services.userService.employeeService;

import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.domainModel.exceptions.DomainValidationException;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.domainModel.users.Employee.EmployeeRole;

import java.util.List;
import java.util.UUID;

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public UUID createEmployee(String name, EmployeeRole role) {
        Employee employee = new Employee(UUID.randomUUID(), name, role);

        return employeeRepository.save(employee);
    }

    public UUID updateEmployee(UUID employeeId, String name, EmployeeRole role) {
        if (employeeId == null) {
            throw new DomainValidationException("missing required field \"employee\"");
        }
        employeeRepository.findById(employeeId);

        Employee updatedEmployee = new Employee(employeeId, name, role);

        return employeeRepository.save(updatedEmployee);
    }

    public Employee findById(UUID employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByRole(EmployeeRole role) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.hasRole(role))
                .toList();
    }

    public void deleteById(UUID employeeId) {
        employeeRepository.findById(employeeId);

        employeeRepository.deleteById(employeeId);
    }
}
