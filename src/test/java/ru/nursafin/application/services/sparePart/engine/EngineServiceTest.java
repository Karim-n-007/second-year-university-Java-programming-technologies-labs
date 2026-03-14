package ru.nursafin.application.services.sparePart.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.services.sparePartService.engine.EngineService;
import ru.nursafin.domainModel.entities.sparePart.engine.FuelType;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.valueObjects.EngineDisplacement;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage.InMemoryCarModelsDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryEngineDataStorage;
import ru.nursafin.infrastructure.repositories.inMemoryCarModelRepository.InMemoryCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryEngineRepository.InMemoryEngineRepository;

import java.util.List;
import java.util.UUID;

public class EngineServiceTest {
    @Test
    void shouldSaveAndFindBodyCorrectly() {
        InMemoryEngineRepository engineSparePartRepository = new InMemoryEngineRepository(new InMemoryEngineDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        EngineService engineService = new EngineService(engineSparePartRepository, carModelRepository);

        UUID engineId1 = UUID.randomUUID();
        Engine engine = new Engine (
                new Power(1000),
                new EngineDisplacement(50),
                FuelType.DIESEL,
                engineId1,
                "superEngine",
                new Money(10000)
        );
        engineService.createNewEngine(engine);

        
        Assertions.assertEquals(engine, engineService.findById(engineId1));
    }

    @Test
    void shouldHaveTheSameIdAfterSaveInEngineRepository() {
        InMemoryEngineRepository engineSparePartRepository = new InMemoryEngineRepository(new InMemoryEngineDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        EngineService engineService = new EngineService(engineSparePartRepository, carModelRepository);

        UUID engineId1 = UUID.randomUUID();
        Engine engine = new Engine (
                new Power(1000),
                new EngineDisplacement(50),
                FuelType.DIESEL,
                engineId1,
                "superEngine",
                new Money(10000)
        );

        
        UUID bodyId2 = engineService.createNewEngine(engine);
        
        
        Assertions.assertEquals(engineId1, bodyId2);
    }

    @Test
    void shouldReturnAllEngines() {
        InMemoryEngineRepository engineSparePartRepository = new InMemoryEngineRepository(new InMemoryEngineDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        EngineService engineService = new EngineService(engineSparePartRepository, carModelRepository);

        UUID engineId1 = UUID.randomUUID();
        UUID engineId2 = UUID.randomUUID();

        Engine engine1 = new Engine (
                new Power(1000),
                new EngineDisplacement(50),
                FuelType.DIESEL,
                engineId1,
                "superEngine",
                new Money(10000)
        );

        Engine engine2 = new Engine (
                new Power(1000),
                new EngineDisplacement(50),
                FuelType.DIESEL,
                engineId2,
                "superEngine",
                new Money(10000)
        );

        engineService.createNewEngine(engine1);
        engineService.createNewEngine(engine2);
        List<Engine> engines = engineService.getAllEngines();


        Assertions.assertEquals(2, engines.size());
        Assertions.assertTrue(engines.contains(engine1));
        Assertions.assertTrue(engines.contains(engine2));
    }

    @Test
    void priceShouldSaveCorrectly() {
        UUID engineId = UUID.randomUUID();
        Engine engine = new Engine (
                new Power(1000),
                new EngineDisplacement(50),
                FuelType.DIESEL,
                engineId,
                "superEngine",
                new Money(10000)
        );

        Assertions.assertEquals(engine.getPrice(), new Money(10000));
    }

    @Test
    void shouldUpdatePrice() {
        InMemoryEngineRepository engineSparePartRepository = new InMemoryEngineRepository(new InMemoryEngineDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        EngineService engineService = new EngineService(engineSparePartRepository, carModelRepository);

        UUID engineId = UUID.randomUUID();
        Engine engine = new Engine (
                new Power(1000),
                new EngineDisplacement(50),
                FuelType.DIESEL,
                engineId,
                "superEngine",
                new Money(10000)
        );
        Money newMoney = new Money(100500);


        engineService.createNewEngine(engine);
        engineService.updateEnginePrice(engineId, newMoney);


        Assertions.assertEquals(newMoney, engineService.findById(engineId).getPrice());
    }

    @Test
    void shouldThrowNotFoundException() {
        InMemoryEngineRepository engineSparePartRepository = new InMemoryEngineRepository(new InMemoryEngineDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        EngineService engineService = new EngineService(engineSparePartRepository, carModelRepository);

        UUID engineId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();

        Engine engine = new Engine (
                new Power(1000),
                new EngineDisplacement(50),
                FuelType.DIESEL,
                engineId,
                "superEngine",
                new Money(10000)
        );


        engineService.createNewEngine(engine);


        Assertions.assertThrows(EntityNotFoundException.class, () -> engineService.findById(fakeId));
    }
}
