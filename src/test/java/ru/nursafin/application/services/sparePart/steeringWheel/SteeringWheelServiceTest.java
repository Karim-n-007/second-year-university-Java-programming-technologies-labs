package ru.nursafin.application.services.sparePart.steeringWheel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.services.sparePartService.steeringWheel.SteeringWheelService;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheelMaterial;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheelType;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage.InMemoryCarModelsDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemorySteeringWheelDataStorage;
import ru.nursafin.infrastructure.repositories.inMemoryCarModelRepository.InMemoryCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemorySteeringWheelRepository.InMemorySteeringWheelRepository;


import java.util.List;
import java.util.UUID;

public class SteeringWheelServiceTest {
    @Test
    void shouldSaveAndFindSteeringWheelCorrectly() {
        InMemorySteeringWheelRepository engineSparePartRepository = new InMemorySteeringWheelRepository(new InMemorySteeringWheelDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        SteeringWheelService SteeringWheelService = new SteeringWheelService(engineSparePartRepository, carModelRepository);

        UUID steeringWheelId = UUID.randomUUID();
        SteeringWheel steeringWheel = new SteeringWheel(
                SteeringWheelType.FUNCTIONAL,
                SteeringWheelMaterial.LEATHER,
                steeringWheelId,
                "good steering wheel",
                new Money(10000)
        );

        SteeringWheelService.createNewSteeringWheel(steeringWheel);


        Assertions.assertEquals(steeringWheel, SteeringWheelService.findById(steeringWheelId));
    }

    @Test
    void shouldHaveTheSameIdAfterSaveInSteeringWheelRepository() {
        InMemorySteeringWheelRepository engineSparePartRepository = new InMemorySteeringWheelRepository(new InMemorySteeringWheelDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        SteeringWheelService SteeringWheelService = new SteeringWheelService(engineSparePartRepository, carModelRepository);

        UUID SteeringWheelId1 = UUID.randomUUID();
        SteeringWheel steeringWheel = new SteeringWheel(
                SteeringWheelType.FUNCTIONAL,
                SteeringWheelMaterial.LEATHER,
                SteeringWheelId1,
                "good steering wheel",
                new Money(10000)
        );

        UUID SteeringWheelId2 = SteeringWheelService.createNewSteeringWheel(steeringWheel);


        Assertions.assertEquals(SteeringWheelId1, SteeringWheelId2);
    }

    @Test
    void shouldReturnAllSteeringWheels() {
        InMemorySteeringWheelRepository gearboxSparePartRepository = new InMemorySteeringWheelRepository(new InMemorySteeringWheelDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        SteeringWheelService SteeringWheelService = new SteeringWheelService(gearboxSparePartRepository, carModelRepository);

        UUID SteeringWheelId1 = UUID.randomUUID();
        UUID SteeringWheelId2 = UUID.randomUUID();

        SteeringWheel steeringWheel1 = new SteeringWheel(
                SteeringWheelType.FUNCTIONAL,
                SteeringWheelMaterial.LEATHER,
                SteeringWheelId1,
                "good steering wheel",
                new Money(10000)
        );


        SteeringWheel steeringWheel2 = new SteeringWheel(
                SteeringWheelType.FUNCTIONAL,
                SteeringWheelMaterial.LEATHER,
                SteeringWheelId2,
                "good steering wheel",
                new Money(10000)
        );



        SteeringWheelService.createNewSteeringWheel(steeringWheel1);
        SteeringWheelService.createNewSteeringWheel(steeringWheel2);
        List<SteeringWheel> steeringWheels = SteeringWheelService.getAllSteeringWheels();


        Assertions.assertEquals(2, steeringWheels.size());
        Assertions.assertTrue(steeringWheels.contains(steeringWheel1));
        Assertions.assertTrue(steeringWheels.contains(steeringWheel2));
    }

    @Test
    void priceShouldSaveCorrectly() {
        UUID SteeringWheelId = UUID.randomUUID();
        SteeringWheel steeringWheel = new SteeringWheel(
                SteeringWheelType.FUNCTIONAL,
                SteeringWheelMaterial.LEATHER,
                SteeringWheelId,
                "good steering wheel",
                new Money(10000)
        );

        Assertions.assertEquals(steeringWheel.getPrice(), new Money(10000));
    }

    @Test
    void shouldUpdatePrice() {
        InMemorySteeringWheelRepository engineSparePartRepository = new InMemorySteeringWheelRepository(new InMemorySteeringWheelDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        SteeringWheelService SteeringWheelService = new SteeringWheelService(engineSparePartRepository, carModelRepository);

        UUID steeringWheelId = UUID.randomUUID();
        SteeringWheel steeringWheel = new SteeringWheel(
                SteeringWheelType.FUNCTIONAL,
                SteeringWheelMaterial.LEATHER,
                steeringWheelId,
                "good steering wheel",
                new Money(10000)
        );
        Money newMoney = new Money(100500);


        SteeringWheelService.createNewSteeringWheel(steeringWheel);
        SteeringWheelService.updateSteeringWheelPrice(steeringWheelId, newMoney);


        Assertions.assertEquals(newMoney, SteeringWheelService.findById(steeringWheelId).getPrice());
    }

    @Test
    void shouldThrowNotFoundException() {
        InMemorySteeringWheelRepository engineSparePartRepository = new InMemorySteeringWheelRepository(new InMemorySteeringWheelDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        SteeringWheelService SteeringWheelService = new SteeringWheelService(engineSparePartRepository, carModelRepository);

        UUID steeringWheelId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();

        SteeringWheel steeringWheel = new SteeringWheel(
                SteeringWheelType.FUNCTIONAL,
                SteeringWheelMaterial.LEATHER,
                steeringWheelId,
                "good steering wheel",
                new Money(10000)
        );


        SteeringWheelService.createNewSteeringWheel(steeringWheel);


        Assertions.assertThrows(EntityNotFoundException.class, () -> SteeringWheelService.findById(fakeId));
    }
}
