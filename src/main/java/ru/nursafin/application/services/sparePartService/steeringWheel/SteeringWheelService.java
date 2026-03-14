package ru.nursafin.application.services.sparePartService.steeringWheel;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.SteeringWheelSparePartRepository;

import java.util.List;
import java.util.UUID;

public class SteeringWheelService {
    private final SteeringWheelSparePartRepository steeringWheelRepository;
    private final CarModelRepository carModelRepository;

    public SteeringWheelService(SteeringWheelSparePartRepository steeringWheelRepository, CarModelRepository carModelRepository) {
        this.steeringWheelRepository = steeringWheelRepository;
        this.carModelRepository = carModelRepository;
    }

    public UUID createNewSteeringWheel(SteeringWheel steeringWheel) {
        return steeringWheelRepository.save(steeringWheel);
    }

    public void updateSteeringWheelPrice(UUID steeringWheelId, Money price) {
        SteeringWheel steeringWheel = steeringWheelRepository.findById(steeringWheelId);

        steeringWheel.setPrice(price);
    }

    public void addNewCarCompatibleWithSteeringWheel(SteeringWheel steeringWheel, UUID carModelId) {
        carModelRepository.findById(carModelId);
        steeringWheel.addCompatibleCar(carModelId);
    }

    public SteeringWheel findById(UUID steeringWheelId) {
        return steeringWheelRepository.findById(steeringWheelId);
    }

    public List<SteeringWheel> getAllSteeringWheels() {
        return steeringWheelRepository.findAll();
    }
}
