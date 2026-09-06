package ru.nursafin.domainModel.entities.order;

import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;
import ru.nursafin.domainModel.statuses.ReadyCarOrderStatus;

import java.util.UUID;

public class OrderReadyCarModel {
    private ReadyCarOrderStatus status = ReadyCarOrderStatus.PLACED;

    private final UUID id;
    private final UUID clientId;
    private final UUID employeeId;
    private final CarModel carModel;
    private final Money priceAtCreateOrderMoment;

    public OrderReadyCarModel(UUID id, UUID clientId, UUID employeeId, CarModel carModel, Money priceAtCreateMoment) {
        if (id == null || clientId == null || employeeId == null || carModel == null || priceAtCreateMoment == null) {
            throw new DomainValidationException("Some information about order is null");
        }

        this.id = id;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.carModel = carModel;
        this.priceAtCreateOrderMoment = priceAtCreateMoment;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public Money getPriceAtCreateOrderMoment() {
        return priceAtCreateOrderMoment;
    }

    public ReadyCarOrderStatus getStatus() {
        return status;
    }

    public void changeStatus(ReadyCarOrderStatus newStatus) {
        if (newStatus == null) {
            throw new DomainValidationException("Order status is null");
        }
        if (!status.canChangeTo(newStatus)) {
            throw new DomainValidationException(
                    "Order " + id + " cannot change status from " + status + " to " + newStatus);
        }

        this.status = newStatus;
    }
}
