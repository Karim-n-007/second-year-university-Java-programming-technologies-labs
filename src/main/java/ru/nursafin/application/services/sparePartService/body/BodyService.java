package ru.nursafin.application.services.sparePartService.body;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.BodySparePartRepository;

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
        return bodyRepository.save(body);
    }

    public void updateBodyPrice(UUID bodyId, Money price) {
       Body body = bodyRepository.findById(bodyId);

       body.setPrice(price);
    }

    public void addNewCarCompatibleWithBody(Body body, UUID carModelId) {
        carModelRepository.findById(carModelId);
        body.addCompatibleCar(carModelId);
    }

    public Body findById(UUID bodyId) {
        return bodyRepository.findById(bodyId);
    }

    public List<Body> getAllBody() {
        return bodyRepository.findAll();
    }
}
