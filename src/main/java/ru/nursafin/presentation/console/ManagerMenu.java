package ru.nursafin.presentation.console;

import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.filters.testDrive.TestDriveFilter;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequestStatus;
import ru.nursafin.domainModel.statuses.CustomCarOrderStatus;
import ru.nursafin.domainModel.statuses.ReadyCarOrderStatus;
import ru.nursafin.presentation.ApplicationContext;

import java.util.UUID;

public class ManagerMenu extends Menu {
    private final ApplicationContext context;

    public ManagerMenu(ConsoleIo io, ApplicationContext context) {
        super(io);
        this.context = context;

        register("1", "Show cars with filters", this::showCars);
        register("2", "Show car details", this::showCar);
        register("3", "Show orders for cars in stock", this::showReadyOrders);
        register("4", "Show orders for custom configured cars", this::showCustomOrders);
        register("5", "Change status of an order for a car in stock", this::changeReadyOrderStatus);
        register("6", "Change status of a custom configuration order", this::changeCustomOrderStatus);
        register("7", "Show cars available for test drive", this::showTestDriveCars);
        register("8", "Add a car to the test drive list", this::addTestDriveCar);
        register("9", "Remove a car from the test drive list", this::removeTestDriveCar);
        register("10", "Show test drive requests", this::showTestDriveRequests);
        register("11", "Change test drive request status", this::changeTestDriveRequestStatus);
    }

    @Override
    protected String title() {
        return "Sales manager";
    }

    private void showCars() {
        context.getSuitableCars().getSuitableCars(CarFilterReader.read(io))
                .forEach(carModel -> io.print(ConsoleFormatter.shortCarModel(carModel)));
    }

    private void showCar() {
        UUID carModelId = io.readUuid("Car model id: ");

        io.print(ConsoleFormatter.fullCarModel(context.getCarModelService().findById(carModelId)));
    }

    private void showReadyOrders() {
        OrderFilter filter = new OrderFilter()
                .withClientId(io.readOptionalUuid("Filter by client: "))
                .withEmployeeId(io.readOptionalUuid("Filter by manager: "));

        context.getReadyCarModelOrderService().findAll(filter)
                .forEach(order -> io.print(ConsoleFormatter.readyOrder(order)));
    }

    private void showCustomOrders() {
        OrderFilter filter = new OrderFilter()
                .withClientId(io.readOptionalUuid("Filter by client: "))
                .withEmployeeId(io.readOptionalUuid("Filter by manager: "));

        context.getCustomCarModelOrderService().findAll(filter)
                .forEach(order -> io.print(ConsoleFormatter.customOrder(order)));
    }

    private void changeReadyOrderStatus() {
        UUID orderId = io.readUuid("Order id: ");
        io.print("Current status: " + context.getReadyCarModelOrderService().findById(orderId).getStatus());

        context.getReadyCarModelOrderService()
                .changeStatus(orderId, io.readEnum("New status", ReadyCarOrderStatus.class));

        io.print("Status updated: " + ConsoleFormatter.readyOrder(
                context.getReadyCarModelOrderService().findById(orderId)));
    }

    private void changeCustomOrderStatus() {
        UUID orderId = io.readUuid("Order id: ");
        io.print("Current status: " + context.getCustomCarModelOrderService().findById(orderId).getStatus());

        context.getCustomCarModelOrderService()
                .changeStatus(orderId, io.readEnum("New status", CustomCarOrderStatus.class));

        io.print("Status updated: " + ConsoleFormatter.customOrder(
                context.getCustomCarModelOrderService().findById(orderId)));
    }

    private void showTestDriveCars() {
        context.getTestDriveService().getCarsAvailableForTestDrive()
                .forEach(carModel -> io.print(ConsoleFormatter.shortCarModel(carModel)));
    }

    private void addTestDriveCar() {
        context.getTestDriveService().addCarToTestDriveList(io.readUuid("Car model id: "));

        io.print("Car added to the test drive list");
    }

    private void removeTestDriveCar() {
        context.getTestDriveService().removeCarFromTestDriveList(io.readUuid("Car model id: "));

        io.print("Car removed from the test drive list");
    }

    private void showTestDriveRequests() {
        TestDriveFilter filter = new TestDriveFilter()
                .withClientId(io.readOptionalUuid("Filter by client: "))
                .withCarModelId(io.readOptionalUuid("Filter by car model: "))
                .withStatus(io.readOptionalEnum("Filter by status", TestDriveRequestStatus.class));

        context.getTestDriveService().findAll(filter)
                .forEach(request -> io.print(ConsoleFormatter.testDriveRequest(request)));
    }

    private void changeTestDriveRequestStatus() {
        UUID requestId = io.readUuid("Test drive request id: ");

        context.getTestDriveService()
                .changeStatus(requestId, io.readEnum("New status", TestDriveRequestStatus.class));

        io.print("Test drive request status updated");
    }
}
