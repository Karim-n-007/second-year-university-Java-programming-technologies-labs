package ru.nursafin.application.services.sparePartService.steeringWheel;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.SteeringWheelSparePartRepository;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

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
        if (steeringWheel == null) {
            throw new DomainValidationException("missing required component \"Steering wheel\"");
        }

        return steeringWheelRepository.save(steeringWheel);
    }

    public void updateSteeringWheelPrice(UUID steeringWheelId, Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }

        SteeringWheel steeringWheel = steeringWheelRepository.findById(steeringWheelId);
        steeringWheel.setPrice(price);

        steeringWheelRepository.save(steeringWheel);
    }

    public void addNewCarCompatibleWithSteeringWheel(UUID steeringWheelId, UUID carModelId) {
        SteeringWheel steeringWheel = steeringWheelRepository.findById(steeringWheelId);
        carModelRepository.findById(carModelId);

        steeringWheel.addCompatibleCar(carModelId);

        steeringWheelRepository.save(steeringWheel);
    }

    public SteeringWheel findById(UUID steeringWheelId) {
        return steeringWheelRepository.findById(steeringWheelId);
    }

    public List<SteeringWheel> getAllSteeringWheels() {
        return steeringWheelRepository.findAll();
    }

    public void deleteById(UUID steeringWheelId) {
        steeringWheelRepository.findById(steeringWheelId);

        steeringWheelRepository.deleteById(steeringWheelId);
    }
}
