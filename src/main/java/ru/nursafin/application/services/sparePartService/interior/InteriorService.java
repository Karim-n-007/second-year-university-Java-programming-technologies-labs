package ru.nursafin.application.services.sparePartService.interior;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.InteriorSparePartRepository;

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
        return interiorRepository.save(interior);
    }

    public void updateInteriorPrice(UUID interiorId, Money price) {
        Interior interior = interiorRepository.findById(interiorId);

        interior.setPrice(price);
    }

    public void addNewCarCompatibleWithInterior(Interior interior, UUID carModelId) {
        carModelRepository.findById(carModelId);
        interior.addCompatibleCar(carModelId);
    }

    public Interior findById(UUID interiorId) {
        return interiorRepository.findById(interiorId);
    }

    public List<Interior> getAllInteriors() {
        return interiorRepository.findAll();
    }
}
