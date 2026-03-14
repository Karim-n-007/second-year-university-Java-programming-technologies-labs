package ru.nursafin.application.services.sparePartService.wheels;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.WheelsSparePartRepository;

import java.util.List;
import java.util.UUID;

public class WheelsService {
    private final WheelsSparePartRepository WheelsRepository;
    private final CarModelRepository carModelRepository;

    public WheelsService(WheelsSparePartRepository wheelsRepository, CarModelRepository carModelRepository) {
        WheelsRepository = wheelsRepository;
        this.carModelRepository = carModelRepository;
    }

    public UUID createNewWheels(Wheels Wheels) {
        return WheelsRepository.save(Wheels);
    }

    public void updateWheelsPrice(UUID WheelsId, Money price) {
        Wheels Wheels = WheelsRepository.findById(WheelsId);

        Wheels.setPrice(price);
    }

    public void addNewCarCompatibleWithWheels(Wheels wheels, UUID carModelId) {
        carModelRepository.findById(carModelId);
        wheels.addCompatibleCar(carModelId);
    }

    public Wheels findById(UUID WheelId) {
        return WheelsRepository.findById(WheelId);
    }

    public List<Wheels> getAllWheels() {
        return WheelsRepository.findAll();
    }
}
