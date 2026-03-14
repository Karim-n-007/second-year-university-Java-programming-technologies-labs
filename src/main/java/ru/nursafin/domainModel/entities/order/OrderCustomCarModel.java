package ru.nursafin.domainModel.entities.order;

import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.statuses.CarPurchaseOrderStatus;

import java.util.UUID;

public class OrderCustomCarModel {
    private CarPurchaseOrderStatus status = new CarPurchaseOrderStatus.HasBeenPlaced();

    private final UUID clientId;
    private final UUID employeeId;

    private final CarModel carModelId;

    private final Body body;
    private final Engine engine;
    private final Gearbox gearbox;
    private final SteeringWheel steeringWheel;
    private final Interior interior;
    private final Wheels wheels;
    private final Money priceAtCreateOrderMoment;

    public OrderCustomCarModel(UUID clientId, UUID employeeId, CarModel carModelId, Body body, Engine engine, Gearbox gearbox, SteeringWheel steeringWheel, Interior interior, Wheels wheels, Money priceAtCreateOrderMoment) {
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.carModelId = carModelId;
        this.body = body;
        this.engine = engine;
        this.gearbox = gearbox;
        this.steeringWheel = steeringWheel;
        this.interior = interior;
        this.wheels = wheels;
        this.priceAtCreateOrderMoment = priceAtCreateOrderMoment;
    }


    public void setStatus(CarPurchaseOrderStatus status) {
        this.status = status;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public CarModel getCarModel() {
        return carModelId;
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

    public CarPurchaseOrderStatus getStatus() {
        return status;
    }

    public Money getPriceAtCreateOrderMoment() {
        return priceAtCreateOrderMoment;
    }
}
