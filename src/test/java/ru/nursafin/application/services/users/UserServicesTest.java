package ru.nursafin.application.services.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.application.services.orderService.EmployeeAssignment;
import ru.nursafin.application.services.userService.clientService.ClientService;
import ru.nursafin.application.services.userService.employeeService.EmployeeService;
import ru.nursafin.domainModel.exceptions.DomainValidationException;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.domainModel.users.Employee.EmployeeRole;
import ru.nursafin.domainModel.users.client.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServicesTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ClientService clientService;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldCreateClient() {
        when(clientRepository.save(any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Client.class).getId());

        UUID clientId = clientService.createClient("Ivan", LocalDate.of(1990, 1, 1), "+70000000000", "ivan@mail.ru");

        ArgumentCaptor<Client> savedClient = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(savedClient.capture());

        Assertions.assertEquals(clientId, savedClient.getValue().getId());
        Assertions.assertEquals("Ivan", savedClient.getValue().getName());
        Assertions.assertEquals(LocalDate.of(1990, 1, 1), savedClient.getValue().getBirthday());
        Assertions.assertEquals("+70000000000", savedClient.getValue().getNumber());
        Assertions.assertEquals("ivan@mail.ru", savedClient.getValue().getEmail());
    }

    @Test
    void shouldUpdateClientKeepingIdentifier() {
        UUID clientId = UUID.randomUUID();
        when(clientRepository.findById(clientId)).thenReturn(mock(Client.class));

        clientService.updateClient(clientId, "Petr", LocalDate.of(1991, 2, 2), "+70000000001", "petr@mail.ru");

        ArgumentCaptor<Client> savedClient = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(savedClient.capture());

        Assertions.assertEquals(clientId, savedClient.getValue().getId());
        Assertions.assertEquals("Petr", savedClient.getValue().getName());
    }

    @Test
    void shouldThrowExceptionWhenClientIdentifierIsMissing() {
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> clientService.updateClient(null, "Petr", LocalDate.of(1991, 2, 2), "+7", "petr@mail.ru")
        );
    }

    @Test
    void shouldThrowExceptionWhenClientDataIsNull() {
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> clientService.createClient(null, LocalDate.of(1991, 2, 2), "+7", "petr@mail.ru")
        );
    }

    @Test
    void shouldFindDeleteAndListClients() {
        UUID clientId = UUID.randomUUID();
        Client client = mock(Client.class);

        when(clientRepository.findById(clientId)).thenReturn(client);
        when(clientRepository.findAll()).thenReturn(List.of(client));

        Assertions.assertEquals(client, clientService.findById(clientId));
        Assertions.assertEquals(List.of(client), clientService.getAllClients());

        clientService.deleteById(clientId);
        verify(clientRepository).deleteById(clientId);
    }

    @Test
    void shouldCreateEmployeeWithRole() {
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Employee.class).getId());

        UUID employeeId = employeeService.createEmployee("Maria", EmployeeRole.SYSTEM_ADMINISTRATOR);

        ArgumentCaptor<Employee> savedEmployee = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(savedEmployee.capture());

        Assertions.assertEquals(employeeId, savedEmployee.getValue().getId());
        Assertions.assertEquals(EmployeeRole.SYSTEM_ADMINISTRATOR, savedEmployee.getValue().getRole());
        Assertions.assertTrue(savedEmployee.getValue().hasRole(EmployeeRole.SYSTEM_ADMINISTRATOR));
        Assertions.assertFalse(savedEmployee.getValue().hasRole(EmployeeRole.SALES_MANAGER));
    }

    @Test
    void shouldUpdateEmployeeKeepingIdentifier() {
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.findById(employeeId)).thenReturn(mock(Employee.class));

        employeeService.updateEmployee(employeeId, "Maria", EmployeeRole.WAREHOUSE_ADMINISTRATOR);

        ArgumentCaptor<Employee> savedEmployee = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(savedEmployee.capture());

        Assertions.assertEquals(employeeId, savedEmployee.getValue().getId());
        Assertions.assertEquals(EmployeeRole.WAREHOUSE_ADMINISTRATOR, savedEmployee.getValue().getRole());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIsInvalid() {
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> employeeService.createEmployee(" ", EmployeeRole.SALES_MANAGER)
        );
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> employeeService.updateEmployee(null, "Maria", EmployeeRole.SALES_MANAGER)
        );
    }

    @Test
    void shouldFilterEmployeesByRole() {
        Employee manager = new Employee(UUID.randomUUID(), "Manager", EmployeeRole.SALES_MANAGER);
        Employee warehouse = new Employee(UUID.randomUUID(), "Warehouse", EmployeeRole.WAREHOUSE_ADMINISTRATOR);

        when(employeeRepository.findAll()).thenReturn(List.of(manager, warehouse));

        Assertions.assertEquals(List.of(manager), employeeService.getEmployeesByRole(EmployeeRole.SALES_MANAGER));
        Assertions.assertEquals(List.of(manager, warehouse), employeeService.getAllEmployees());
    }

    @Test
    void shouldFindAndDeleteEmployee() {
        UUID employeeId = UUID.randomUUID();
        Employee employee = mock(Employee.class);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        Assertions.assertEquals(employee, employeeService.findById(employeeId));

        employeeService.deleteById(employeeId);
        verify(employeeRepository).deleteById(employeeId);
    }

    @Test
    void shouldAssignOnlySalesManagers() {
        Employee firstManager = new Employee(UUID.randomUUID(), "First", EmployeeRole.SALES_MANAGER);
        Employee secondManager = new Employee(UUID.randomUUID(), "Second", EmployeeRole.SALES_MANAGER);
        Employee warehouse = new Employee(UUID.randomUUID(), "Warehouse", EmployeeRole.WAREHOUSE_ADMINISTRATOR);

        when(employeeRepository.findAll()).thenReturn(List.of(warehouse, firstManager, secondManager));

        EmployeeAssignment assignment = new EmployeeAssignment(employeeRepository, new Random(1));

        for (int attempt = 0; attempt < 20; attempt++) {
            Assertions.assertTrue(assignment.assignSalesManager().hasRole(EmployeeRole.SALES_MANAGER));
        }
    }

    @Test
    void shouldThrowExceptionWhenThereIsNoSalesManager() {
        when(employeeRepository.findAll())
                .thenReturn(List.of(new Employee(UUID.randomUUID(), "Warehouse", EmployeeRole.WAREHOUSE_ADMINISTRATOR)));

        EmployeeAssignment assignment = new EmployeeAssignment(employeeRepository);

        Assertions.assertThrows(EntityNotFoundException.class, assignment::assignSalesManager);
    }
}
