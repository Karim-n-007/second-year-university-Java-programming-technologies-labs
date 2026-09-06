package ru.nursafin.presentation;

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
import ru.nursafin.domainModel.users.Employee.EmployeeRole;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class DemoDataInitializer {
    private final ApplicationContext context;

    public DemoDataInitializer(ApplicationContext context) {
        this.context = context;
    }

    public void initialize() {
        createEmployees();
        createClients();

        UUID sedanBodyId = createBody("Sedan body", BodyType.SEDAN, Money.ZERO);
        UUID wagonBodyId = createBody("Station wagon body", BodyType.STATION_WAGON, Money.ZERO);

        UUID petrolEngineId = createEngine("2.0 Turbo, gasoline", 184, 1998, FuelType.GASOLINE, Money.ZERO);
        UUID dieselEngineId = createEngine("2.0 D, diesel", 190, 1995, FuelType.DIESEL, Money.ZERO);

        UUID automaticGearboxId = createGearbox("Automatic 8AT", GearboxType.AUTOMATIC, Money.ZERO);
        UUID mechanicalGearboxId = createGearbox("Manual 6MT", GearboxType.MECHANICAL, new Money(-30_000));

        UUID standardSteeringWheelId = createSteeringWheel("Sport leather (Standard)",
                SteeringWheelType.SPORTY, SteeringWheelMaterial.LEATHER, Money.ZERO);
        UUID sportSteeringWheelId = createSteeringWheel("M-Sport heated",
                SteeringWheelType.SPORTY, SteeringWheelMaterial.LEATHER, new Money(25_000));

        UUID fabricInteriorId = createInterior("Fabric Graphite", InteriorType.FABRIC, "graphite", Money.ZERO);
        UUID leatherInteriorId = createInterior("Leather Dakota", InteriorType.LEATHER, "beige", new Money(110_000));
        UUID performanceInteriorId = createInterior("Sport Performance", InteriorType.LEATHER, "black",
                new Money(160_000));

        UUID standardWheelsId = createWheels("17'' Standard", WheelSeason.ALL_SEASONS, Money.ZERO);
        UUID aeroWheelsId = createWheels("18'' Aero", WheelSeason.SUMMER, new Money(45_000));
        UUID sportWheelsId = createWheels("19'' M-Sport", WheelSeason.SUMMER, new Money(95_000));

        UUID bmw320iId = context.getCarModelService().createNewCarModel("320i", "BMW", "white",
                new Money(3_000_000), Drive.REAR_WHEEL_DRIVE, sedanBodyId, petrolEngineId, automaticGearboxId,
                standardSteeringWheelId, fabricInteriorId, standardWheelsId);

        UUID bmw330iId = context.getCarModelService().createNewCarModel("330i", "BMW", "black",
                new Money(3_600_000), Drive.ALL_WHEEL_DRIVE, sedanBodyId, petrolEngineId, automaticGearboxId,
                standardSteeringWheelId, fabricInteriorId, standardWheelsId);

        UUID audiA4Id = context.getCarModelService().createNewCarModel("A4 Avant", "Audi", "blue",
                new Money(3_200_000), Drive.FRONT_WHEEL_DRIVE, wagonBodyId, dieselEngineId, mechanicalGearboxId,
                standardSteeringWheelId, fabricInteriorId, standardWheelsId);

        List<UUID> bmwModels = List.of(bmw320iId, bmw330iId);
        List<UUID> allModels = List.of(bmw320iId, bmw330iId, audiA4Id);

        allModels.forEach(carModelId -> {
            context.getBodyService().addNewCarCompatibleWithBody(sedanBodyId, carModelId);
            context.getEngineService().addNewCarCompatibleWithEngine(petrolEngineId, carModelId);
            context.getGearboxService().addNewCarCompatibleWithGearbox(automaticGearboxId, carModelId);
            context.getSteeringWheelService().addNewCarCompatibleWithSteeringWheel(standardSteeringWheelId, carModelId);
            context.getInteriorService().addNewCarCompatibleWithInterior(fabricInteriorId, carModelId);
            context.getWheelsService().addNewCarCompatibleWithWheels(standardWheelsId, carModelId);
        });

        context.getBodyService().addNewCarCompatibleWithBody(wagonBodyId, audiA4Id);
        context.getEngineService().addNewCarCompatibleWithEngine(dieselEngineId, audiA4Id);

        bmwModels.forEach(carModelId -> {
            context.getGearboxService().addNewCarCompatibleWithGearbox(mechanicalGearboxId, carModelId);
            context.getSteeringWheelService().addNewCarCompatibleWithSteeringWheel(sportSteeringWheelId, carModelId);
            context.getInteriorService().addNewCarCompatibleWithInterior(leatherInteriorId, carModelId);
            context.getWheelsService().addNewCarCompatibleWithWheels(aeroWheelsId, carModelId);
            context.getWheelsService().addNewCarCompatibleWithWheels(sportWheelsId, carModelId);
        });

        context.getInteriorService().addNewCarCompatibleWithInterior(performanceInteriorId, bmw330iId);

        context.getTestDriveService().addCarToTestDriveList(bmw320iId);
        context.getTestDriveService().addCarToTestDriveList(bmw330iId);
    }

    private void createEmployees() {
        context.getEmployeeService().createEmployee("Ivan Sokolov", EmployeeRole.SALES_MANAGER);
        context.getEmployeeService().createEmployee("Petr Volkov", EmployeeRole.SALES_MANAGER);
        context.getEmployeeService().createEmployee("Anna Orlova", EmployeeRole.WAREHOUSE_ADMINISTRATOR);
        context.getEmployeeService().createEmployee("Maria Titova", EmployeeRole.SYSTEM_ADMINISTRATOR);
    }

    private void createClients() {
        context.getClientService().createClient("Alexey Ivanov", LocalDate.of(1995, 4, 12),
                "+79990000001", "alexey@example.com");
        context.getClientService().createClient("Olga Smirnova", LocalDate.of(1988, 11, 3),
                "+79990000002", "olga@example.com");
    }

    private UUID createBody(String name, BodyType bodyType, Money price) {
        return context.getBodyService().createNewBody(new Body(bodyType, UUID.randomUUID(), name, price));
    }

    private UUID createEngine(String name, int power, int displacement, FuelType fuelType, Money price) {
        Engine engine = new Engine(new Power(power), new EngineDisplacement(displacement), fuelType,
                UUID.randomUUID(), name, price);

        return context.getEngineService().createNewEngine(engine);
    }

    private UUID createGearbox(String name, GearboxType gearboxType, Money price) {
        return context.getGearboxService().createNewGearbox(new Gearbox(gearboxType, UUID.randomUUID(), name, price));
    }

    private UUID createSteeringWheel(String name, SteeringWheelType type, SteeringWheelMaterial material, Money price) {
        SteeringWheel steeringWheel = new SteeringWheel(type, material, UUID.randomUUID(), name, price);

        return context.getSteeringWheelService().createNewSteeringWheel(steeringWheel);
    }

    private UUID createInterior(String name, InteriorType interiorType, String color, Money price) {
        Interior interior = new Interior(interiorType, color, UUID.randomUUID(), name, price);

        return context.getInteriorService().createNewInterior(interior);
    }

    private UUID createWheels(String name, WheelSeason season, Money price) {
        return context.getWheelsService().createNewWheels(new Wheels(season, UUID.randomUUID(), name, price));
    }
}
