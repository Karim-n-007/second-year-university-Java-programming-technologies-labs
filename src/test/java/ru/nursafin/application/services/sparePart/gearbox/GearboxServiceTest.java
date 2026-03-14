package ru.nursafin.application.services.sparePart.gearbox;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.services.sparePartService.gearbox.GearboxService;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.gearbox.GearboxType;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage.InMemoryCarModelsDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryGearboxDataStorage;
import ru.nursafin.infrastructure.repositories.inMemoryCarModelRepository.InMemoryCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryGearboxRepository.InMemoryGearboxRepository;


import java.util.List;
import java.util.UUID;

public class GearboxServiceTest {
    @Test
    void shouldSaveAndFindGearboxCorrectly() {
        InMemoryGearboxRepository engineSparePartRepository = new InMemoryGearboxRepository(new InMemoryGearboxDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        GearboxService gearboxService = new GearboxService(engineSparePartRepository, carModelRepository);

        UUID gearboxId = UUID.randomUUID();
        Gearbox gearbox = new Gearbox(
                GearboxType.MECHANICAL,
                gearboxId,
                "best gearbox",
                new Money(10000)
        );
        gearboxService.createNewGearbox(gearbox);


        Assertions.assertEquals(gearbox, gearboxService.findById(gearboxId));
    }

    @Test
    void shouldHaveTheSameIdAfterSaveInGearboxRepository() {
        InMemoryGearboxRepository engineSparePartRepository = new InMemoryGearboxRepository(new InMemoryGearboxDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        GearboxService gearboxService = new GearboxService(engineSparePartRepository, carModelRepository);

        UUID gearboxId1 = UUID.randomUUID();
        Gearbox gearbox = new Gearbox(
                GearboxType.MECHANICAL,
                gearboxId1,
                "best gearbox",
                new Money(10000)
        );
        
        UUID gearboxId2 = gearboxService.createNewGearbox(gearbox);


        Assertions.assertEquals(gearboxId1, gearboxId2);
    }

    @Test
    void shouldReturnAllGearboxes() {
        InMemoryGearboxRepository gearboxSparePartRepository = new InMemoryGearboxRepository(new InMemoryGearboxDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        GearboxService gearboxService = new GearboxService(gearboxSparePartRepository, carModelRepository);

        UUID gearboxId1 = UUID.randomUUID();
        UUID gearboxId2 = UUID.randomUUID();

        Gearbox gearbox1 = new Gearbox(
                GearboxType.MECHANICAL,
                gearboxId1,
                "best gearbox",
                new Money(10000)
        );

        Gearbox gearbox2 = new Gearbox(
                GearboxType.MECHANICAL,
                gearboxId2,
                "best gearbox",
                new Money(10000)
        );


        gearboxService.createNewGearbox(gearbox1);
        gearboxService.createNewGearbox(gearbox2);

        List<Gearbox> gearboxes = gearboxService.getAllGearboxes();


        Assertions.assertEquals(2, gearboxes.size());
        Assertions.assertTrue(gearboxes.contains(gearbox1));
        Assertions.assertTrue(gearboxes.contains(gearbox2));
    }

    @Test
    void priceShouldSaveCorrectly() {
        UUID gearboxId = UUID.randomUUID();
        Gearbox gearbox = new Gearbox(
                GearboxType.MECHANICAL,
                gearboxId,
                "best gearbox",
                new Money(10000)
        );

        Assertions.assertEquals(gearbox.getPrice(), new Money(10000));
    }

    @Test
    void shouldUpdatePrice() {
        InMemoryGearboxRepository engineSparePartRepository = new InMemoryGearboxRepository(new InMemoryGearboxDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        GearboxService gearboxService = new GearboxService(engineSparePartRepository, carModelRepository);

        UUID gearboxId = UUID.randomUUID();
        Gearbox gearbox = new Gearbox(
                GearboxType.MECHANICAL,
                gearboxId,
                "best gearbox",
                new Money(10000)
        );
        Money newMoney = new Money(100500);


        gearboxService.createNewGearbox(gearbox);
        gearboxService.updateGearboxPrice(gearboxId, newMoney);


        Assertions.assertEquals(newMoney, gearboxService.findById(gearboxId).getPrice());
    }

    @Test
    void shouldThrowNotFoundException() {
        InMemoryGearboxRepository engineSparePartRepository = new InMemoryGearboxRepository(new InMemoryGearboxDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        GearboxService gearboxService = new GearboxService(engineSparePartRepository, carModelRepository);

        UUID gearboxId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();

        Gearbox gearbox = new Gearbox(
                GearboxType.MECHANICAL,
                gearboxId,
                "best gearbox",
                new Money(10000)
        );


        gearboxService.createNewGearbox(gearbox);


        Assertions.assertThrows(EntityNotFoundException.class, () -> gearboxService.findById(fakeId));
    }
}
