package ru.nursafin.application.services.sparePart.interior;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.services.sparePartService.interior.InteriorService;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.interior.InteriorType;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage.InMemoryCarModelsDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryInteriorDataStorage;
import ru.nursafin.infrastructure.repositories.inMemoryCarModelRepository.InMemoryCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryInteriorRepository.InMemoryInteriorRepository;


import java.util.List;
import java.util.UUID;

public class InteriorServiceTest {
    @Test
    void shouldSaveAndFindInteriorCorrectly() {
        InMemoryInteriorRepository engineSparePartRepository = new InMemoryInteriorRepository(new InMemoryInteriorDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        InteriorService interiorService = new InteriorService(engineSparePartRepository, carModelRepository);

        UUID interiorId = UUID.randomUUID();
        Interior interior = new Interior(
                InteriorType.LEATHER,
                "white",
                interiorId,
                "Modern Interior",
                new Money(10000)
        );
        
        interiorService.createNewInterior(interior);


        Assertions.assertEquals(interior, interiorService.findById(interiorId));
    }

    @Test
    void shouldHaveTheSameIdAfterSaveInInteriorRepository() {
        InMemoryInteriorRepository engineSparePartRepository = new InMemoryInteriorRepository(new InMemoryInteriorDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        InteriorService interiorService = new InteriorService(engineSparePartRepository, carModelRepository);

        UUID interiorId1 = UUID.randomUUID();
        Interior interior = new Interior(
                InteriorType.LEATHER,
                "white",
                interiorId1,
                "Modern Interior",
                new Money(10000)
        );

        UUID interiorId2 = interiorService.createNewInterior(interior);


        Assertions.assertEquals(interiorId1, interiorId2);
    }

    @Test
    void shouldReturnAllInteriors() {
        InMemoryInteriorRepository gearboxSparePartRepository = new InMemoryInteriorRepository(new InMemoryInteriorDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        InteriorService interiorService = new InteriorService(gearboxSparePartRepository, carModelRepository);

        UUID interiorId1 = UUID.randomUUID();
        UUID interiorId2 = UUID.randomUUID();

        Interior interior1 = new Interior(
                InteriorType.LEATHER,
                "white",
                interiorId1,
                "Modern Interior",
                new Money(10000)
        );

        Interior interior2 = new Interior(
                InteriorType.LEATHER,
                "white",
                interiorId2,
                "Modern Interior",
                new Money(10000)
        );


        interiorService.createNewInterior(interior1);
        interiorService.createNewInterior(interior2);
        List<Interior> interiors = interiorService.getAllInteriors();


        Assertions.assertEquals(2, interiors.size());
        Assertions.assertTrue(interiors.contains(interior1));
        Assertions.assertTrue(interiors.contains(interior2));
    }

    @Test
    void priceShouldSaveCorrectly() {
        UUID interiorId = UUID.randomUUID();
        Interior interior = new Interior(
                InteriorType.LEATHER,
                "white",
                interiorId,
                "Modern Interior",
                new Money(10000)
        );

        Assertions.assertEquals(interior.getPrice(), new Money(10000));
    }

    @Test
    void shouldUpdatePrice() {
        InMemoryInteriorRepository engineSparePartRepository = new InMemoryInteriorRepository(new InMemoryInteriorDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        InteriorService interiorService = new InteriorService(engineSparePartRepository, carModelRepository);

        UUID interiorId = UUID.randomUUID();
        Interior interior = new Interior(
                InteriorType.LEATHER,
                "white",
                interiorId,
                "Modern Interior",
                new Money(10000)
        );
        Money newMoney = new Money(100500);


        interiorService.createNewInterior(interior);
        interiorService.updateInteriorPrice(interiorId, newMoney);


        Assertions.assertEquals(newMoney, interiorService.findById(interiorId).getPrice());
    }

    @Test
    void shouldThrowNotFoundException() {
        InMemoryInteriorRepository engineSparePartRepository = new InMemoryInteriorRepository(new InMemoryInteriorDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        InteriorService interiorService = new InteriorService(engineSparePartRepository, carModelRepository);

        UUID interiorId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();

        Interior interior = new Interior(
                InteriorType.LEATHER,
                "white",
                interiorId,
                "Modern Interior",
                new Money(10000)
        );


        interiorService.createNewInterior(interior);


        Assertions.assertThrows(EntityNotFoundException.class, () -> interiorService.findById(fakeId));
    }
}
