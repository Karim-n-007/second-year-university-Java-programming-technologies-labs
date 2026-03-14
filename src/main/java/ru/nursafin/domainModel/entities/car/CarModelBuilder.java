package ru.nursafin.domainModel.entities.car;

import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Drive;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.UUID;

public class CarModelBuilder {
    private UUID id = null;
    private String name = null;
    private String brand = null;
    private Money price = null;
    private Drive drive = null;

    private Body body = null;
    private Engine engine = null;
    private Gearbox gearbox = null;
    private SteeringWheel steeringWheel = null;
    private Interior interior = null;
    private Wheels wheels = null;

    public CarModelBuilder withId(UUID id) {
        if (id == null) {
            throw new DomainValidationException("id is null");
        }
        this.id = id;

        return this;
    }

    public CarModelBuilder withName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("name is null");
        }
        this.name = name;

        return this;
    }

    public CarModelBuilder withBrand(String brand) {
        if (brand == null || brand.isBlank()) {
            throw new DomainValidationException("brand is null");
        }
        this.brand = brand;

        return this;
    }

    public CarModelBuilder withPrice(Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }
        this.price = price;

        return this;
    }

    public CarModelBuilder withDrive(Drive drive) {
        if (drive == null) {
            throw new DomainValidationException("drive is null");
        }
        this.drive = drive;

        return this;
    }

    public CarModelBuilder withBody(Body body) {
        if (body == null) {
            throw new DomainValidationException("body is null");
        }
        this.body = body;

        return this;
    }

    public CarModelBuilder withEngine(Engine engine) {
        if (engine == null) {
            throw new DomainValidationException("engine is null");
        }
        this.engine = engine;

        return this;
    }

    public CarModelBuilder withGearbox(Gearbox gearbox) {
        if (gearbox == null) {
            throw new DomainValidationException("gearbox is null");
        }
        this.gearbox = gearbox;

        return this;
    }

    public CarModelBuilder withSteeringWheel(SteeringWheel wheels) {
        if (wheels == null) {
            throw new DomainValidationException("wheels is null");
        }
        this.steeringWheel = wheels;

        return this;
    }

    public CarModelBuilder withInterior(Interior interior) {
        if (interior == null) {
            throw new DomainValidationException("interior is null");
        }
        this.interior = interior;

        return this;
    }

    public CarModelBuilder withWheels(Wheels wheels) {
        if (wheels == null) {
            throw new DomainValidationException("wheels is null");
        }
        this.wheels = wheels;

        return this;
    }

    public CarModel build() {
        if (id == null || name == null || brand == null ||
                price == null || drive == null || body == null ||
                engine == null || gearbox == null || steeringWheel == null ||
                interior == null || wheels == null) {
            throw new DomainValidationException("one or more value is null");
        }

        return new CarModel(id, name, brand, price, drive, body, engine, gearbox, steeringWheel, interior, wheels);
    }
}
