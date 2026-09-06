package ru.nursafin.presentation.console;

import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.body.BodyType;
import ru.nursafin.domainModel.entities.sparePart.engine.Drive;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.engine.FuelType;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.gearbox.GearboxType;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.interior.InteriorType;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheelMaterial;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheelType;
import ru.nursafin.domainModel.entities.sparePart.wheels.WheelSeason;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.EngineDisplacement;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;
import ru.nursafin.presentation.ApplicationContext;

import java.util.UUID;

public class WarehouseAdministratorMenu extends Menu {
    private final ApplicationContext context;

    public WarehouseAdministratorMenu(ConsoleIo io, ApplicationContext context) {
        super(io);
        this.context = context;

        register("1", "Show cars", this::showCars);
        register("2", "Show car details", this::showCar);
        register("3", "Add a new car", this::createCar);
        register("4", "Update car information", this::updateCar);
        register("5", "Show spare parts", this::showSpareParts);
        register("6", "Add a new spare part", this::createSparePart);
        register("7", "Update spare part price", this::updateSparePartPrice);
        register("8", "Mark spare part compatible with a car", this::addCompatibility);
    }

    @Override
    protected String title() {
        return "Warehouse administrator";
    }

    private void showCars() {
        context.getCarModelService().getAllCarModels()
                .forEach(carModel -> io.print(ConsoleFormatter.shortCarModel(carModel)));
    }

    private void showCar() {
        io.print(ConsoleFormatter.fullCarModel(
                context.getCarModelService().findById(io.readUuid("Car model id: "))));
    }

    private void createCar() {
        UUID carModelId = context.getCarModelService().createNewCarModel(
                io.readRequiredText("Model: "),
                io.readRequiredText("Brand: "),
                io.readRequiredText("Color: "),
                io.readMoney("Base price: "),
                io.readEnum("Drive", Drive.class),
                io.readUuid("Body id: "),
                io.readUuid("Engine id: "),
                io.readUuid("Gearbox id: "),
                io.readUuid("Steering wheel id: "),
                io.readUuid("Interior id: "),
                io.readUuid("Wheels id: "));

        io.print("Car added: " + carModelId);
    }

    private void updateCar() {
        UUID carModelId = io.readUuid("Car model id: ");
        io.print(ConsoleFormatter.fullCarModel(context.getCarModelService().findById(carModelId)));

        context.getCarModelService().updateCarModel(
                carModelId,
                io.readRequiredText("Model: "),
                io.readRequiredText("Brand: "),
                io.readRequiredText("Color: "),
                io.readMoney("Base price: "),
                io.readEnum("Drive", Drive.class),
                io.readUuid("Body id: "),
                io.readUuid("Engine id: "),
                io.readUuid("Gearbox id: "),
                io.readUuid("Steering wheel id: "),
                io.readUuid("Interior id: "),
                io.readUuid("Wheels id: "));

        io.print("Car information updated");
    }

    private void showSpareParts() {
        context.getBodyService().getAllBody().forEach(part -> io.print("Body: " + ConsoleFormatter.sparePart(part)));
        context.getEngineService().getAllEngines().forEach(part -> io.print("Engine: " + ConsoleFormatter.sparePart(part)));
        context.getGearboxService().getAllGearboxes().forEach(part -> io.print("Gearbox: " + ConsoleFormatter.sparePart(part)));
        context.getSteeringWheelService().getAllSteeringWheels().forEach(part -> io.print("Steering wheel: " + ConsoleFormatter.sparePart(part)));
        context.getInteriorService().getAllInteriors().forEach(part -> io.print("Interior: " + ConsoleFormatter.sparePart(part)));
        context.getWheelsService().getAllWheels().forEach(part -> io.print("Wheels: " + ConsoleFormatter.sparePart(part)));
    }

    private void createSparePart() {
        SparePartKind kind = io.readEnum("Spare part kind", SparePartKind.class);
        String name = io.readRequiredText("Name: ");
        Money price = io.readMoney("Surcharge: ");
        UUID sparePartId = UUID.randomUUID();

        UUID createdId = switch (kind) {
            case BODY -> context.getBodyService().createNewBody(
                    new Body(io.readEnum("Body type", BodyType.class), sparePartId, name, price));
            case ENGINE -> context.getEngineService().createNewEngine(new Engine(
                    new Power(io.readInt("Power (hp): ")),
                    new EngineDisplacement(io.readInt("Displacement (cm3): ")),
                    io.readEnum("Fuel type", FuelType.class), sparePartId, name, price));
            case GEARBOX -> context.getGearboxService().createNewGearbox(
                    new Gearbox(io.readEnum("Gearbox type", GearboxType.class), sparePartId, name, price));
            case STEERING_WHEEL -> context.getSteeringWheelService().createNewSteeringWheel(new SteeringWheel(
                    io.readEnum("Steering wheel type", SteeringWheelType.class),
                    io.readEnum("Material", SteeringWheelMaterial.class), sparePartId, name, price));
            case INTERIOR -> context.getInteriorService().createNewInterior(new Interior(
                    io.readEnum("Interior type", InteriorType.class),
                    io.readRequiredText("Interior color: "), sparePartId, name, price));
            case WHEELS -> context.getWheelsService().createNewWheels(
                    new Wheels(io.readEnum("Season", WheelSeason.class), sparePartId, name, price));
        };

        io.print("Spare part added: " + createdId);
    }

    private void updateSparePartPrice() {
        SparePartKind kind = io.readEnum("Spare part kind", SparePartKind.class);
        UUID sparePartId = io.readUuid("Spare part id: ");
        Money price = io.readMoney("New surcharge: ");

        switch (kind) {
            case BODY -> context.getBodyService().updateBodyPrice(sparePartId, price);
            case ENGINE -> context.getEngineService().updateEnginePrice(sparePartId, price);
            case GEARBOX -> context.getGearboxService().updateGearboxPrice(sparePartId, price);
            case STEERING_WHEEL -> context.getSteeringWheelService().updateSteeringWheelPrice(sparePartId, price);
            case INTERIOR -> context.getInteriorService().updateInteriorPrice(sparePartId, price);
            case WHEELS -> context.getWheelsService().updateWheelsPrice(sparePartId, price);
        }

        io.print("Price updated");
    }

    private void addCompatibility() {
        SparePartKind kind = io.readEnum("Spare part kind", SparePartKind.class);
        UUID sparePartId = io.readUuid("Spare part id: ");
        UUID carModelId = io.readUuid("Car model id: ");

        switch (kind) {
            case BODY -> context.getBodyService().addNewCarCompatibleWithBody(sparePartId, carModelId);
            case ENGINE -> context.getEngineService().addNewCarCompatibleWithEngine(sparePartId, carModelId);
            case GEARBOX -> context.getGearboxService().addNewCarCompatibleWithGearbox(sparePartId, carModelId);
            case STEERING_WHEEL ->
                    context.getSteeringWheelService().addNewCarCompatibleWithSteeringWheel(sparePartId, carModelId);
            case INTERIOR -> context.getInteriorService().addNewCarCompatibleWithInterior(sparePartId, carModelId);
            case WHEELS -> context.getWheelsService().addNewCarCompatibleWithWheels(sparePartId, carModelId);
        }

        io.print("Compatibility updated");
    }
}
