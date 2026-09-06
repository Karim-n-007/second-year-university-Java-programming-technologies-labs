package ru.nursafin.application.services.sparePartService.engine;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.EngineSparePartRepository;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

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
        if (engine == null) {
            throw new DomainValidationException("missing required component \"Engine\"");
        }

        return engineRepository.save(engine);
    }

    public void updateEnginePrice(UUID engineId, Money price) {
        if (price == null) {
            throw new DomainValidationException("price is null");
        }

        Engine engine = engineRepository.findById(engineId);
        engine.setPrice(price);

        engineRepository.save(engine);
    }

    public void addNewCarCompatibleWithEngine(UUID engineId, UUID carModelId) {
        Engine engine = engineRepository.findById(engineId);
        carModelRepository.findById(carModelId);

        engine.addCompatibleCar(carModelId);

        engineRepository.save(engine);
    }

    public Engine findById(UUID engineId) {
        return engineRepository.findById(engineId);
    }

    public List<Engine> getAllEngines() {
        return engineRepository.findAll();
    }

    public void deleteById(UUID engineId) {
        engineRepository.findById(engineId);

        engineRepository.deleteById(engineId);
    }
}
