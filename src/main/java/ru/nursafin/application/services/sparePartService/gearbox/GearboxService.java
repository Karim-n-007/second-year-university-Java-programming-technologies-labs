package ru.nursafin.application.services.sparePartService.gearbox;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.GearboxSparePartRepository;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

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
        if (gearbox == null) {
            throw new DomainValidationException("missing required component \"Gearbox\"");
        }

        return gearboxRepository.save(gearbox);
    }

    public void updateGearboxPrice(UUID gearboxId, Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }

        Gearbox gearbox = gearboxRepository.findById(gearboxId);
        gearbox.setPrice(price);

        gearboxRepository.save(gearbox);
    }

    public void addNewCarCompatibleWithGearbox(UUID gearboxId, UUID carModelId) {
        Gearbox gearbox = gearboxRepository.findById(gearboxId);
        carModelRepository.findById(carModelId);

        gearbox.addCompatibleCar(carModelId);

        gearboxRepository.save(gearbox);
    }

    public Gearbox findById(UUID gearboxId) {
        return gearboxRepository.findById(gearboxId);
    }

    public List<Gearbox> getAllGearboxes() {
        return gearboxRepository.findAll();
    }

    public void deleteById(UUID gearboxId) {
        gearboxRepository.findById(gearboxId);

        gearboxRepository.deleteById(gearboxId);
    }
}
