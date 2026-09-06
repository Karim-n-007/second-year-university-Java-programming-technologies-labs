package ru.nursafin.presentation.console;

import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.filters.testDrive.TestDriveFilter;
import ru.nursafin.presentation.ApplicationContext;

import java.util.UUID;

public class ClientMenu extends Menu {
    private final ApplicationContext context;

    public ClientMenu(ConsoleIo io, ApplicationContext context) {
        super(io);
        this.context = context;

        register("1", "Show cars with filters", this::showCars);
        register("2", "Show car details", this::showCar);
        register("3", "Order a car in stock", this::createReadyCarOrder);
        register("4", "Order a car with custom configuration", this::createCustomCarOrder);
        register("5", "Book a test drive", this::requestTestDrive);
        register("6", "My orders", this::showMyOrders);
        register("7", "My test drive requests", this::showMyTestDriveRequests);
        register("8", "Show clients (to pick an identifier)", this::showClients);
    }

    @Override
    protected String title() {
        return "Client";
    }

    private void showCars() {
        context.getSuitableCars().getSuitableCars(CarFilterReader.read(io))
                .forEach(carModel -> io.print(ConsoleFormatter.shortCarModel(carModel)));
    }

    private void showCar() {
        UUID carModelId = io.readUuid("Car model id: ");

        io.print(ConsoleFormatter.fullCarModel(context.getCarModelService().findById(carModelId)));
    }

    private void createReadyCarOrder() {
        UUID clientId = io.readUuid("Your id: ");
        UUID carModelId = io.readUuid("Car model id: ");

        UUID orderId = context.getReadyCarModelOrderService().createOrder(clientId, carModelId);

        io.print("Order created: " + orderId);
        io.print(ConsoleFormatter.readyOrder(context.getReadyCarModelOrderService().findById(orderId)));
    }

    private void createCustomCarOrder() {
        UUID clientId = io.readUuid("Your id: ");
        UUID carModelId = io.readUuid("Car model id: ");

        io.print("Available components:");
        context.getBodyService().getAllBody().forEach(part -> io.print("Body: " + ConsoleFormatter.sparePart(part)));
        context.getEngineService().getAllEngines().forEach(part -> io.print("Engine: " + ConsoleFormatter.sparePart(part)));
        context.getGearboxService().getAllGearboxes().forEach(part -> io.print("Gearbox: " + ConsoleFormatter.sparePart(part)));
        context.getSteeringWheelService().getAllSteeringWheels().forEach(part -> io.print("Steering wheel: " + ConsoleFormatter.sparePart(part)));
        context.getInteriorService().getAllInteriors().forEach(part -> io.print("Interior: " + ConsoleFormatter.sparePart(part)));
        context.getWheelsService().getAllWheels().forEach(part -> io.print("Wheels: " + ConsoleFormatter.sparePart(part)));

        UUID bodyId = io.readUuid("Body: ");
        UUID engineId = io.readUuid("Engine: ");
        UUID gearboxId = io.readUuid("Gearbox: ");
        UUID steeringWheelId = io.readUuid("Steering wheel: ");
        UUID interiorId = io.readUuid("Interior: ");
        UUID wheelsId = io.readUuid("Wheels: ");

        UUID orderId = context.getCustomCarModelOrderService().createOrder(clientId, carModelId, bodyId, engineId,
                gearboxId, steeringWheelId, interiorId, wheelsId);

        io.print("Order created: " + orderId);
        io.print(ConsoleFormatter.customOrder(context.getCustomCarModelOrderService().findById(orderId)));
    }

    private void requestTestDrive() {
        io.print("Cars available for test drive:");
        context.getTestDriveService().getCarsAvailableForTestDrive()
                .forEach(carModel -> io.print(ConsoleFormatter.shortCarModel(carModel)));

        UUID clientId = io.readUuid("Your id: ");
        UUID carModelId = io.readUuid("Car model id: ");

        UUID requestId = context.getTestDriveService()
                .requestTestDrive(clientId, carModelId, io.readDateTime("Test drive date and time"));

        io.print("Test drive request created: " + requestId);
    }

    private void showMyOrders() {
        OrderFilter filter = new OrderFilter().withClientId(io.readUuid("Your id: "));

        io.print("Orders for cars in stock:");
        context.getReadyCarModelOrderService().findAll(filter)
                .forEach(order -> io.print(ConsoleFormatter.readyOrder(order)));

        io.print("Orders for custom configured cars:");
        context.getCustomCarModelOrderService().findAll(filter)
                .forEach(order -> io.print(ConsoleFormatter.customOrder(order)));
    }

    private void showMyTestDriveRequests() {
        TestDriveFilter filter = new TestDriveFilter().withClientId(io.readUuid("Your id: "));

        context.getTestDriveService().findAll(filter)
                .forEach(request -> io.print(ConsoleFormatter.testDriveRequest(request)));
    }

    private void showClients() {
        context.getClientService().getAllClients()
                .forEach(client -> io.print(ConsoleFormatter.client(client)));
    }
}
