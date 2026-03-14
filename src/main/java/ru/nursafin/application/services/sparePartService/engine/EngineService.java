package ru.nursafin.application.services.sparePartService.engine;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.EngineSparePartRepository;

import java.util.List;
import java.util.UUID;

public class EngineService {
    private final EngineSparePartRepository engineRepository;
    private final CarModelRepository carModelRepository;

    public EngineService(EngineSparePartRepository engineRepository, CarModelRepository carModelRepository) {
        this.engineRepository = engineRepository;
        this.carModelRepository = carModelRepository;
    }

    public UUID createNewEngine(Engine engine) {
        return engineRepository.save(engine);
    }

    public void updateEnginePrice(UUID engineId, Money price) {
        Engine engine = engineRepository.findById(engineId);

        engine.setPrice(price);
    }

    public void addNewCarCompatibleWithEngine(Engine engine, UUID carModelId) {
        carModelRepository.findById(carModelId);
        engine.addCompatibleCar(carModelId);
    }

    public Engine findById(UUID engineId) {
        return engineRepository.findById(engineId);
    }

    public List<Engine> getAllEngines() {
        return engineRepository.findAll();
    }
}
