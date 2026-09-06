package ru.nursafin.presentation.console;

import ru.nursafin.domainModel.users.Employee.EmployeeRole;
import ru.nursafin.presentation.ApplicationContext;

import java.util.UUID;

public class SystemAdministratorMenu extends Menu {
    private final ApplicationContext context;

    public SystemAdministratorMenu(ConsoleIo io, ApplicationContext context) {
        super(io);
        this.context = context;

        register("1", "Show clients", this::showClients);
        register("2", "Create a client", this::createClient);
        register("3", "Update a client", this::updateClient);
        register("4", "Delete a client", this::deleteClient);
        register("5", "Show employees", this::showEmployees);
        register("6", "Create an employee", this::createEmployee);
        register("7", "Update an employee", this::updateEmployee);
        register("8", "Delete an employee", this::deleteEmployee);
        register("9", "Delete a car", this::deleteCarModel);
        register("10", "Delete an order for a car in stock", this::deleteReadyOrder);
        register("11", "Delete a custom configuration order", this::deleteCustomOrder);
        register("12", "Delete a test drive request", this::deleteTestDriveRequest);
    }

    @Override
    protected String title() {
        return "System administrator";
    }

    private void showClients() {
        context.getClientService().getAllClients()
                .forEach(client -> io.print(ConsoleFormatter.client(client)));
    }

    private void createClient() {
        UUID clientId = context.getClientService().createClient(
                io.readRequiredText("Name: "),
                io.readDate("Birthday"),
                io.readRequiredText("Phone: "),
                io.readRequiredText("Email: "));

        io.print("Client created: " + clientId);
    }

    private void updateClient() {
        UUID clientId = io.readUuid("Client id: ");

        context.getClientService().updateClient(
                clientId,
                io.readRequiredText("Name: "),
                io.readDate("Birthday"),
                io.readRequiredText("Phone: "),
                io.readRequiredText("Email: "));

        io.print("Client updated");
    }

    private void deleteClient() {
        context.getClientService().deleteById(io.readUuid("Client id: "));

        io.print("Client deleted");
    }

    private void showEmployees() {
        context.getEmployeeService().getAllEmployees()
                .forEach(employee -> io.print(ConsoleFormatter.employee(employee)));
    }

    private void createEmployee() {
        UUID employeeId = context.getEmployeeService().createEmployee(
                io.readRequiredText("Name: "),
                io.readEnum("Role", EmployeeRole.class));

        io.print("Employee created: " + employeeId);
    }

    private void updateEmployee() {
        UUID employeeId = io.readUuid("Employee id: ");

        context.getEmployeeService().updateEmployee(
                employeeId,
                io.readRequiredText("Name: "),
                io.readEnum("Role", EmployeeRole.class));

        io.print("Employee updated");
    }

    private void deleteEmployee() {
        context.getEmployeeService().deleteById(io.readUuid("Employee id: "));

        io.print("Employee deleted");
    }

    private void deleteCarModel() {
        context.getCarModelService().deleteById(io.readUuid("Car model id: "));

        io.print("Car deleted");
    }

    private void deleteReadyOrder() {
        context.getReadyCarModelOrderService().deleteById(io.readUuid("Order id: "));

        io.print("Order deleted");
    }

    private void deleteCustomOrder() {
        context.getCustomCarModelOrderService().deleteById(io.readUuid("Order id: "));

        io.print("Order deleted");
    }

    private void deleteTestDriveRequest() {
        context.getTestDriveService().deleteById(io.readUuid("Test drive request id: "));

        io.print("Test drive request deleted");
    }
}
