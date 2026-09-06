package ru.nursafin.domainModel.entities.order;

import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;
import ru.nursafin.domainModel.statuses.CustomCarOrderStatus;

import java.util.UUID;

public class OrderCustomCarModel {
    private CustomCarOrderStatus status = CustomCarOrderStatus.PLACED;

    private final UUID id;
    private final UUID clientId;
    private final UUID employeeId;

    private final CarModel carModel;

    private final Body body;
    private final Engine engine;
    private final Gearbox gearbox;
    private final SteeringWheel steeringWheel;
    private final Interior interior;
    private final Wheels wheels;
    private final Money priceAtCreateOrderMoment;

    public OrderCustomCarModel(UUID id, UUID clientId, UUID employeeId, CarModel carModel, Body body, Engine engine,
                               Gearbox gearbox, SteeringWheel steeringWheel, Interior interior, Wheels wheels,
                               Money priceAtCreateOrderMoment) {
        if (id == null || clientId == null || employeeId == null || carModel == null || body == null || engine == null
                || gearbox == null || steeringWheel == null || interior == null || wheels == null
                || priceAtCreateOrderMoment == null) {
            throw new DomainValidationException("Some information about order is null");
        }

        this.id = id;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.carModel = carModel;
        this.body = body;
        this.engine = engine;
        this.gearbox = gearbox;
        this.steeringWheel = steeringWheel;
        this.interior = interior;
        this.wheels = wheels;
        this.priceAtCreateOrderMoment = priceAtCreateOrderMoment;
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

    public Body getBody() {
        return body;
    }

    public Engine getEngine() {
        return engine;
    }

    public Gearbox getGearbox() {
        return gearbox;
    }

    public SteeringWheel getSteeringWheel() {
        return steeringWheel;
    }

    public Interior getInterior() {
        return interior;
    }

    public Wheels getWheels() {
        return wheels;
    }

    public CustomCarOrderStatus getStatus() {
        return status;
    }

    public Money getPriceAtCreateOrderMoment() {
        return priceAtCreateOrderMoment;
    }

    public void changeStatus(CustomCarOrderStatus newStatus) {
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
