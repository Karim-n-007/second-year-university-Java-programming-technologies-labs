package ru.nursafin.presentation.console;

import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.order.OrderCustomCarModel;
import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;
import ru.nursafin.domainModel.entities.sparePart.SparePart;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequest;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.domainModel.users.client.Client;

public final class ConsoleFormatter {
    private ConsoleFormatter() {
    }

    public static String shortCarModel(CarModel carModel) {
        return carModel.getId() + " | " + carModel.getBrand() + " " + carModel.getName()
                + " | " + carModel.getColor() + " | from " + carModel.getBasePrice();
    }

    public static String fullCarModel(CarModel carModel) {
        return """
                Identifier: %s
                Brand and model: %s %s
                Color: %s
                Base price: %s
                Drive: %s
                Body: %s (%s)
                Engine: %s (%s, %s hp, %s cm3)
                Gearbox: %s (%s)
                Steering wheel: %s
                Interior: %s (%s)
                Wheels: %s"""
                .formatted(
                        carModel.getId(),
                        carModel.getBrand(), carModel.getName(),
                        carModel.getColor(),
                        carModel.getBasePrice(),
                        carModel.getDrive(),
                        carModel.getBody().getName(), carModel.getBody().getBodyType(),
                        carModel.getEngine().getName(), carModel.getEngine().getFuelType(),
                        carModel.getEngine().getPower().getValue(),
                        carModel.getEngine().getEngineDisplacement().getValue(),
                        carModel.getGearbox().getName(), carModel.getGearbox().getGearboxType(),
                        carModel.getSteeringWheel().getName(),
                        carModel.getInterior().getName(), carModel.getInterior().getColor(),
                        carModel.getWheels().getName());
    }

    public static String sparePart(SparePart sparePart) {
        return sparePart.getId() + " | " + sparePart.getName() + " | surcharge " + sparePart.getPrice()
                + " | compatible car models: " + sparePart.getCompatibleCars().size();
    }

    public static String readyOrder(OrderReadyCarModel order) {
        return order.getId() + " | " + order.getCarModel().getBrand() + " " + order.getCarModel().getName()
                + " | client " + order.getClientId() + " | manager " + order.getEmployeeId()
                + " | " + order.getStatus() + " | " + order.getPriceAtCreateOrderMoment();
    }

    public static String customOrder(OrderCustomCarModel order) {
        return order.getId() + " | " + order.getCarModel().getBrand() + " " + order.getCarModel().getName()
                + " | client " + order.getClientId() + " | manager " + order.getEmployeeId()
                + " | " + order.getStatus() + " | " + order.getPriceAtCreateOrderMoment();
    }

    public static String testDriveRequest(TestDriveRequest request) {
        return request.getId() + " | car model " + request.getCarModelId() + " | client " + request.getClientId()
                + " | " + request.getStartDateTime().format(ConsoleIo.DATE_TIME_FORMAT)
                + " | " + request.getStatus();
    }

    public static String client(Client client) {
        return client.getId() + " | " + client.getName() + " | " + client.getNumber() + " | " + client.getEmail();
    }

    public static String employee(Employee employee) {
        return employee.getId() + " | " + employee.getName() + " | " + employee.getRole();
    }
}
