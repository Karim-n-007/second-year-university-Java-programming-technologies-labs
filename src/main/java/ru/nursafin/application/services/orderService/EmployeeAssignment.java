package ru.nursafin.application.services.orderService;

import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.domainModel.users.Employee.EmployeeRole;

import java.util.List;
import java.util.Random;

public class EmployeeAssignment {
    private final EmployeeRepository employeeRepository;
    private final Random random;

    public EmployeeAssignment(EmployeeRepository employeeRepository) {
        this(employeeRepository, new Random());
    }

    public EmployeeAssignment(EmployeeRepository employeeRepository, Random random) {
        this.employeeRepository = employeeRepository;
        this.random = random;
    }

    public Employee assignSalesManager() {
        List<Employee> salesManagers = employeeRepository.findAll().stream()
                .filter(employee -> employee.hasRole(EmployeeRole.SALES_MANAGER))
                .toList();

        if (salesManagers.isEmpty()) {
            throw new EntityNotFoundException("There is no sales manager to assign an order to");
        }

        return salesManagers.get(random.nextInt(salesManagers.size()));
    }
}
