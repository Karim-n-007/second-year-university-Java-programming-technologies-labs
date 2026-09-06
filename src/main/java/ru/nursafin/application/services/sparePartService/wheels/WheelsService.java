package ru.nursafin.application.services.sparePartService.wheels;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.WheelsSparePartRepository;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.List;
import java.util.UUID;

public class WheelsService {
    private final WheelsSparePartRepository wheelsRepository;
    private final CarModelRepository carModelRepository;

    public WheelsService(WheelsSparePartRepository wheelsRepository, CarModelRepository carModelRepository) {
        this.wheelsRepository = wheelsRepository;
        this.carModelRepository = carModelRepository;
    }

    public UUID createNewWheels(Wheels wheels) {
        if (wheels == null) {
            throw new DomainValidationException("missing required component \"Wheels\"");
        }

        return wheelsRepository.save(wheels);
    }

    public void updateWheelsPrice(UUID wheelsId, Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }

        Wheels wheels = wheelsRepository.findById(wheelsId);
        wheels.setPrice(price);

        wheelsRepository.save(wheels);
    }

    public void addNewCarCompatibleWithWheels(UUID wheelsId, UUID carModelId) {
        Wheels wheels = wheelsRepository.findById(wheelsId);
        carModelRepository.findById(carModelId);

        wheels.addCompatibleCar(carModelId);

        wheelsRepository.save(wheels);
    }

    public Wheels findById(UUID wheelsId) {
        return wheelsRepository.findById(wheelsId);
    }

    public List<Wheels> getAllWheels() {
        return wheelsRepository.findAll();
    }

    public void deleteById(UUID wheelsId) {
        wheelsRepository.findById(wheelsId);

        wheelsRepository.deleteById(wheelsId);
    }
}
