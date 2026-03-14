package ru.nursafin.application.services.sparePart.body;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.services.sparePartService.body.BodyService;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.body.BodyType;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage.InMemoryCarModelsDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.InMemoryBodyDataStorage;
import ru.nursafin.infrastructure.repositories.inMemoryCarModelRepository.InMemoryCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryBodyRepository.InMemoryBodyRepository;

import java.util.*;

public class BodyServiceTest {
    @Test
    void shouldSaveAndFindBodyCorrectly() {
        InMemoryBodyRepository bodySparePartRepository = new InMemoryBodyRepository(new InMemoryBodyDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        BodyService bodyService = new BodyService(bodySparePartRepository, carModelRepository);

        UUID bodyId1 = UUID.randomUUID();
        Body body = new Body (
                BodyType.SEDAN,
                bodyId1,
                "LADA",
                new Money(10000)
        );


        bodyService.createNewBody(body);


        Assertions.assertEquals(body, bodyService.findById(bodyId1));
    }

    @Test
    void shouldHaveTheSameIdAfterSaveInBodyRepository() {
        InMemoryBodyRepository bodySparePartRepository = new InMemoryBodyRepository(new InMemoryBodyDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        BodyService bodyService = new BodyService(bodySparePartRepository, carModelRepository);

        UUID bodyId1 = UUID.randomUUID();
        Body body = new Body (
                BodyType.SEDAN,
                bodyId1,
                "LADA",
                new Money(10000)
        );


        UUID bodyId2 = bodyService.createNewBody(body);


        Assertions.assertEquals(bodyId1, bodyId2);
    }

    @Test
    void shouldReturnAllBodies() {
        InMemoryBodyRepository bodySparePartRepository = new InMemoryBodyRepository(new InMemoryBodyDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        BodyService bodyService = new BodyService(bodySparePartRepository, carModelRepository);

        UUID bodyId1 = UUID.randomUUID();
        UUID bodyId2 = UUID.randomUUID();

        Body body1 = new Body (
                BodyType.SEDAN,
                bodyId1,
                "LADA",
                new Money(10000)
        );

        Body body2 = new Body (
                BodyType.MINIVAN,
                bodyId2,
                "BMW",
                new Money(100500)
        );


        bodyService.createNewBody(body1);
        bodyService.createNewBody(body2);
        List<Body> bodies = bodyService.getAllBody();


        Assertions.assertEquals(2, bodies.size());
        Assertions.assertTrue(bodies.contains(body1));
        Assertions.assertTrue(bodies.contains(body2));
    }

    @Test
    void priceShouldSaveCorrectly() {
        UUID bodyId = UUID.randomUUID();
        Body body = new Body (
                BodyType.SEDAN,
                bodyId,
                "LADA",
                new Money(10000)
        );


        Assertions.assertEquals(body.getPrice(), new Money(10000));
    }

    @Test
    void shouldUpdatePrice() {
        InMemoryBodyRepository bodySparePartRepository = new InMemoryBodyRepository(new InMemoryBodyDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        BodyService bodyService = new BodyService(bodySparePartRepository, carModelRepository);

        UUID bodyId = UUID.randomUUID();
        Body body = new Body (
                BodyType.SEDAN,
                bodyId,
                "LADA",
                new Money(10000)
        );
        Money newMoney = new Money(100500);


        bodyService.createNewBody(body);
        bodyService.updateBodyPrice(bodyId, newMoney);


        Assertions.assertEquals(newMoney, bodyService.findById(bodyId).getPrice());
    }

    @Test
    void shouldThrowNotFoundException() {
        InMemoryBodyRepository bodySparePartRepository = new InMemoryBodyRepository(new InMemoryBodyDataStorage());
        CarModelRepository carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        BodyService bodyService = new BodyService(bodySparePartRepository, carModelRepository);

        UUID bodyId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();

        Body body = new Body (
                BodyType.SEDAN,
                bodyId,
                "LADA",
                new Money(10000)
        );


        bodyService.createNewBody(body);


        Assertions.assertThrows(EntityNotFoundException.class, () -> bodyService.findById(fakeId));
    }
}
