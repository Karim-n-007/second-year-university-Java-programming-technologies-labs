package ru.nursafin.application.services.sparePartService.body;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.BodySparePartRepository;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.List;
import java.util.UUID;

public class BodyService {
    private final BodySparePartRepository bodyRepository;
    private final CarModelRepository carModelRepository;

    public BodyService(BodySparePartRepository bodyRepository, CarModelRepository carModelRepository) {
        this.bodyRepository = bodyRepository;
        this.carModelRepository = carModelRepository;
    }

    public UUID createNewBody(Body body) {
        if (body == null) {
            throw new DomainValidationException("missing required component \"Body\"");
        }

        return bodyRepository.save(body);
    }

    public void updateBodyPrice(UUID bodyId, Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }

        Body body = bodyRepository.findById(bodyId);
        body.setPrice(price);

        bodyRepository.save(body);
    }

    public void addNewCarCompatibleWithBody(UUID bodyId, UUID carModelId) {
        Body body = bodyRepository.findById(bodyId);
        carModelRepository.findById(carModelId);

        body.addCompatibleCar(carModelId);

        bodyRepository.save(body);
    }

    public Body findById(UUID bodyId) {
        return bodyRepository.findById(bodyId);
    }

    public List<Body> getAllBody() {
        return bodyRepository.findAll();
    }

    public void deleteById(UUID bodyId) {
        bodyRepository.findById(bodyId);

        bodyRepository.deleteById(bodyId);
    }
}
