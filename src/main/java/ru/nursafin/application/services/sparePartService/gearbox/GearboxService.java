package ru.nursafin.application.services.sparePartService.gearbox;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.GearboxSparePartRepository;

import java.util.List;
import java.util.UUID;

public class GearboxService {
    private final GearboxSparePartRepository gearboxRepository;
    private final CarModelRepository carModelRepository;

    public GearboxService(GearboxSparePartRepository gearboxRepository, CarModelRepository carModelRepository) {
        this.gearboxRepository = gearboxRepository;
        this.carModelRepository = carModelRepository;
    }

    public UUID createNewGearbox(Gearbox gearbox) {
        return gearboxRepository.save(gearbox);
    }

    public void updateGearboxPrice(UUID gearboxId, Money price) {
        Gearbox gearbox = gearboxRepository.findById(gearboxId);

        gearbox.setPrice(price);
    }

    public void addNewCarCompatibleWithGearbox(Gearbox gearbox, UUID carModelId) {
        carModelRepository.findById(carModelId);
        gearbox.addCompatibleCar(carModelId);
    }

    public Gearbox findById(UUID gearboxId) {
        return gearboxRepository.findById(gearboxId);
    }

    public List<Gearbox> getAllGearboxes() {
        return gearboxRepository.findAll();
    }
}
