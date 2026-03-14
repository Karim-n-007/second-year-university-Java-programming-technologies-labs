package ru.nursafin.application.services.sparePart.wheels;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.services.sparePartService.wheels.WheelsService;
import ru.nursafin.domainModel.entities.sparePart.wheels.WheelSeason;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage.InMemoryCarModelsDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryWheelsDataStorage;
import ru.nursafin.infrastructure.repositories.inMemoryCarModelRepository.InMemoryCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryWheelsRepository.InMemoryWheelsRepository;

import java.util.List;
import java.util.UUID;

public class WheelsServiceTest {
    @Test
    void shouldSaveAndFindWheelsCorrectly() {
        InMemoryWheelsRepository engineSparePartRepository = new InMemoryWheelsRepository(new InMemoryWheelsDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        WheelsService wheelsService = new WheelsService(engineSparePartRepository, carModelRepository);

        UUID wheelsId = UUID.randomUUID();
        Wheels Wheels = new Wheels (
                WheelSeason.ALL_SEASONS,
                wheelsId,
                "super wheels",
                new Money(10000)
        );

        wheelsService.createNewWheels(Wheels);


        Assertions.assertEquals(Wheels, wheelsService.findById(wheelsId));
    }

    @Test
    void shouldHaveTheSameIdAfterSaveInGearboxRepository() {
        InMemoryWheelsRepository engineSparePartRepository = new InMemoryWheelsRepository(new InMemoryWheelsDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        WheelsService wheelsService = new WheelsService(engineSparePartRepository, carModelRepository);

        UUID wheelsId = UUID.randomUUID();
        Wheels wheels = new Wheels (
                WheelSeason.ALL_SEASONS,
                wheelsId,
                "super wheels",
                new Money(10000)
        );

        UUID WheelsId2 = wheelsService.createNewWheels(wheels);


        Assertions.assertEquals(wheelsId, WheelsId2);
    }

    @Test
    void shouldReturnAllWheels() {
        InMemoryWheelsRepository gearboxSparePartRepository = new InMemoryWheelsRepository(new InMemoryWheelsDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        WheelsService wheelsService = new WheelsService(gearboxSparePartRepository, carModelRepository);

        UUID wheelsId1 = UUID.randomUUID();
        UUID wheelsId2 = UUID.randomUUID();

        Wheels wheels1 = new Wheels (
                WheelSeason.ALL_SEASONS,
                wheelsId1,
                "super wheels",
                new Money(10000)
        );


        Wheels wheels2 = new Wheels (
                WheelSeason.ALL_SEASONS,
                wheelsId2,
                "super wheels",
                new Money(10000)
        );



        wheelsService.createNewWheels(wheels1);
        wheelsService.createNewWheels(wheels2);
        List<Wheels> wheels = wheelsService.getAllWheels();


        Assertions.assertEquals(2, wheels.size());
        Assertions.assertTrue(wheels.contains(wheels1));
        Assertions.assertTrue(wheels.contains(wheels2));
    }

    @Test
    void priceShouldSaveCorrectly() {
        UUID wheelsId = UUID.randomUUID();
        Wheels Wheels = new Wheels (
                WheelSeason.ALL_SEASONS,
                wheelsId,
                "super wheels",
                new Money(10000)
        );

        Assertions.assertEquals(Wheels.getPrice(), new Money(10000));
    }

    @Test
    void shouldUpdatePrice() {
        InMemoryWheelsRepository engineSparePartRepository = new InMemoryWheelsRepository(new InMemoryWheelsDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        WheelsService wheelsService = new WheelsService(engineSparePartRepository, carModelRepository);

        UUID wheelsId = UUID.randomUUID();
        Wheels Wheels = new Wheels (
                WheelSeason.ALL_SEASONS,
                wheelsId,
                "super wheels",
                new Money(10000)
        );
        Money newMoney = new Money(100500);


        wheelsService.createNewWheels(Wheels);
        wheelsService.updateWheelsPrice(wheelsId, newMoney);


        Assertions.assertEquals(newMoney, wheelsService.findById(wheelsId).getPrice());
    }

    @Test
    void shouldThrowNotFoundException() {
        InMemoryWheelsRepository engineSparePartRepository = new InMemoryWheelsRepository(new InMemoryWheelsDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        WheelsService wheelsService = new WheelsService(engineSparePartRepository, carModelRepository);

        UUID wheelsId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();

        Wheels Wheels = new Wheels(
                WheelSeason.ALL_SEASONS,
                wheelsId,
                "super wheels",
                new Money(10000)
        );


        wheelsService.createNewWheels(Wheels);


        Assertions.assertThrows(EntityNotFoundException.class, () -> wheelsService.findById(fakeId));
    }
}
