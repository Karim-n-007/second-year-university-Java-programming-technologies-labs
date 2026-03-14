package ru.nursafin.domainModel.entities.car;

import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Drive;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;

import java.util.UUID;

public class CarModel {
    private final UUID id;
    private final String name;
    private final String brand;
    private final Money basePrice;
    private final Drive drive;

    private final Body body;
    private final Engine engine;
    private final Gearbox gearbox;
    private final SteeringWheel steeringWheel;
    private final Interior interior;
    private final Wheels wheels;

    public CarModel(UUID id, String name, String brand, Money basePrice, Drive drive, Body body, Engine engine, Gearbox gearbox, SteeringWheel steeringWheel, Interior interior, Wheels wheels) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.basePrice = basePrice;
        this.drive = drive;
        this.body = body;
        this.engine = engine;
        this.gearbox = gearbox;
        this.steeringWheel = steeringWheel;
        this.interior = interior;
        this.wheels = wheels;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public Money getBasePrice() {
        return basePrice;
    }

    public Drive getDrive() {
        return drive;
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
}
