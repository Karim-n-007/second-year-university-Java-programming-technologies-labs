package ru.nursafin.application.services.sparePartService.interior;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.InteriorSparePartRepository;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.List;
import java.util.UUID;

public class InteriorService {
    private final InteriorSparePartRepository interiorRepository;
    private final CarModelRepository carModelRepository;

    public InteriorService(InteriorSparePartRepository interiorRepository, CarModelRepository carModelRepository) {
        this.interiorRepository = interiorRepository;
        this.carModelRepository = carModelRepository;
    }

    public UUID createNewInterior(Interior interior) {
        if (interior == null) {
            throw new DomainValidationException("missing required component \"Interior\"");
        }

        return interiorRepository.save(interior);
    }

    public void updateInteriorPrice(UUID interiorId, Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }

        Interior interior = interiorRepository.findById(interiorId);
        interior.setPrice(price);

        interiorRepository.save(interior);
    }

    public void addNewCarCompatibleWithInterior(UUID interiorId, UUID carModelId) {
        Interior interior = interiorRepository.findById(interiorId);
        carModelRepository.findById(carModelId);

        interior.addCompatibleCar(carModelId);

        interiorRepository.save(interior);
    }

    public Interior findById(UUID interiorId) {
        return interiorRepository.findById(interiorId);
    }

    public List<Interior> getAllInteriors() {
        return interiorRepository.findAll();
    }

    public void deleteById(UUID interiorId) {
        interiorRepository.findById(interiorId);

        interiorRepository.deleteById(interiorId);
    }
}
