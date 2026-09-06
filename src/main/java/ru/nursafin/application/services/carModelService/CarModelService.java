package ru.nursafin.application.services.carModelService;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.*;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.car.CarModelBuilder;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Drive;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.List;
import java.util.UUID;

public class CarModelService {
    private final CarModelRepository carModelRepository;

    private final BodySparePartRepository bodyRepository;
    private final EngineSparePartRepository engineRepository;
    private final GearboxSparePartRepository gearboxRepository;
    private final InteriorSparePartRepository interiorRepository;
    private final SteeringWheelSparePartRepository steeringWheelRepository;
    private final WheelsSparePartRepository wheelsRepository;

    public CarModelService(CarModelRepository carModelRepository, BodySparePartRepository bodyRepository, EngineSparePartRepository engineRepository, GearboxSparePartRepository gearboxRepository, InteriorSparePartRepository interiorRepository, SteeringWheelSparePartRepository steeringWheelRepository, WheelsSparePartRepository wheelsRepository) {
        this.carModelRepository = carModelRepository;
        this.bodyRepository = bodyRepository;
        this.engineRepository = engineRepository;
        this.gearboxRepository = gearboxRepository;
        this.interiorRepository = interiorRepository;
        this.steeringWheelRepository = steeringWheelRepository;
        this.wheelsRepository = wheelsRepository;
    }

    public UUID createNewCarModel(String name, String brand, String color, Money basePrice, Drive drive, UUID bodyId,
                                  UUID engineId, UUID gearboxId, UUID steeringWheelId, UUID interiorId, UUID wheelsId) {
        return saveCarModel(UUID.randomUUID(), name, brand, color, basePrice, drive, bodyId, engineId, gearboxId,
                steeringWheelId, interiorId, wheelsId);
    }

    public void updateCarModel(UUID carModelId, String name, String brand, String color, Money basePrice, Drive drive,
                               UUID bodyId, UUID engineId, UUID gearboxId, UUID steeringWheelId, UUID interiorId,
                               UUID wheelsId) {
        if (carModelId == null) {
            throw new DomainValidationException("missing required field \"car model\"");
        }
        carModelRepository.findById(carModelId);

        saveCarModel(carModelId, name, brand, color, basePrice, drive, bodyId, engineId, gearboxId,
                steeringWheelId, interiorId, wheelsId);
    }

    public CarModel findById(UUID carModelId) {
        return carModelRepository.findById(carModelId);
    }

    public List<CarModel> getAllCarModels() {
        return carModelRepository.findAll();
    }

    public void deleteById(UUID carModelId) {
        carModelRepository.findById(carModelId);

        carModelRepository.deleteById(carModelId);
    }

    public Money getModelCarPrice(CarModel carModel) {
        return carModel.getBasePrice()
                .plus(carModel.getBody().getPrice())
                .plus(carModel.getEngine().getPrice())
                .plus(carModel.getGearbox().getPrice())
                .plus(carModel.getSteeringWheel().getPrice())
                .plus(carModel.getInterior().getPrice())
                .plus(carModel.getWheels().getPrice());
    }

    private UUID saveCarModel(UUID carModelId, String name, String brand, String color, Money basePrice, Drive drive,
                              UUID bodyId, UUID engineId, UUID gearboxId, UUID steeringWheelId, UUID interiorId,
                              UUID wheelsId) {
        Body body = bodyRepository.findById(bodyId);
        Engine engine = engineRepository.findById(engineId);
        Gearbox gearbox = gearboxRepository.findById(gearboxId);
        SteeringWheel steeringWheel = steeringWheelRepository.findById(steeringWheelId);
        Interior interior = interiorRepository.findById(interiorId);
        Wheels wheels = wheelsRepository.findById(wheelsId);

        CarModel carModel = new CarModelBuilder()
                .withId(carModelId)
                .withName(name)
                .withBrand(brand)
                .withColor(color)
                .withPrice(basePrice)
                .withDrive(drive)
                .withBody(body)
                .withEngine(engine)
                .withGearbox(gearbox)
                .withSteeringWheel(steeringWheel)
                .withInterior(interior)
                .withWheels(wheels)
                .build();

        return carModelRepository.save(carModel);
    }
}
