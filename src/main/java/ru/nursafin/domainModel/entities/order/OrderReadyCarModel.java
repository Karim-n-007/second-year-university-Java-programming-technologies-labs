package ru.nursafin.domainModel.entities.order;

import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.statuses.CarPurchaseOrderStatus;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.domainModel.users.client.Client;

public class OrderReadyCarModel {
    private CarPurchaseOrderStatus status = new CarPurchaseOrderStatus.HasBeenPlaced();

    private final Client client;
    private final Employee employee;
    private final CarModel carModel;
    private final Money priceAtCreateOrderMoment;

    public OrderReadyCarModel(Client client, Employee employee, CarModel carModel, Money priceAtCreateMoment) {
        this.client = client;
        this.employee = employee;
        this.carModel = carModel;
        this.priceAtCreateOrderMoment = priceAtCreateMoment;
    }

    public Client getClient() {
        return client;
    }

    public Employee getEmployee() {
        return employee;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public Money getPriceAtCreateOrderMoment() {
        return priceAtCreateOrderMoment;
    }

    public CarPurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(CarPurchaseOrderStatus status) {
        this.status = status;
    }
}
