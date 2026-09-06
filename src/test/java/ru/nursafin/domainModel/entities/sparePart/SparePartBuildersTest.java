package ru.nursafin.domainModel.entities.sparePart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.body.BodyBuilder;
import ru.nursafin.domainModel.entities.sparePart.body.BodyType;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.engine.EngineBuilder;
import ru.nursafin.domainModel.entities.sparePart.engine.FuelType;
import ru.nursafin.domainModel.entities.sparePart.gearbox.GearBoxBuilder;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.gearbox.GearboxType;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.interior.InteriorBuilder;
import ru.nursafin.domainModel.entities.sparePart.interior.InteriorType;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheelBuilder;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheelMaterial;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheelType;
import ru.nursafin.domainModel.entities.sparePart.wheels.WheelSeason;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.sparePart.wheels.WheelsBuilder;
import ru.nursafin.domainModel.entities.valueObjects.EngineDisplacement;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.Set;
import java.util.UUID;

public class SparePartBuildersTest {
    private static final UUID CAR_MODEL_ID = UUID.randomUUID();
    private static final UUID ANOTHER_CAR_MODEL_ID = UUID.randomUUID();

    @Test
    void bodyBuilderShouldKeepCompatibleCarModels() {
        UUID bodyId = UUID.randomUUID();

        Body body = new BodyBuilder()
                .withId(bodyId)
                .withName("Sedan body")
                .withPrice(Money.ZERO)
                .withBody(BodyType.SEDAN)
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .withCompatibleCarModelId(ANOTHER_CAR_MODEL_ID)
                .build();

        Assertions.assertEquals(bodyId, body.getId());
        Assertions.assertEquals(BodyType.SEDAN, body.getBodyType());
        Assertions.assertEquals(Set.of(CAR_MODEL_ID, ANOTHER_CAR_MODEL_ID), body.getCompatibleCars());
    }

    @Test
    void engineBuilderShouldKeepCompatibleCarModels() {
        Engine engine = new EngineBuilder()
                .withId(UUID.randomUUID())
                .withName("2.0 Turbo")
                .withPrice(new Money(50_000))
                .withPower(new Power(184))
                .withDisplacement(new EngineDisplacement(1998))
                .withFuelType(FuelType.GASOLINE)
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build();

        Assertions.assertEquals(184, engine.getPower().getValue());
        Assertions.assertEquals(1998, engine.getEngineDisplacement().getValue());
        Assertions.assertEquals(FuelType.GASOLINE, engine.getFuelType());
        Assertions.assertEquals(Set.of(CAR_MODEL_ID), engine.getCompatibleCars());
    }

    @Test
    void gearboxBuilderShouldKeepCompatibleCarModels() {
        Gearbox gearbox = new GearBoxBuilder()
                .withId(UUID.randomUUID())
                .withName("Manual 6MT")
                .withPrice(new Money(-30_000))
                .withType(GearboxType.MECHANICAL)
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build();

        Assertions.assertEquals(GearboxType.MECHANICAL, gearbox.getGearboxType());
        Assertions.assertTrue(gearbox.getPrice().isNegative());
        Assertions.assertEquals(Set.of(CAR_MODEL_ID), gearbox.getCompatibleCars());
    }

    @Test
    void interiorBuilderShouldKeepCompatibleCarModels() {
        Interior interior = new InteriorBuilder()
                .withId(UUID.randomUUID())
                .withName("Leather Dakota")
                .withPrice(new Money(110_000))
                .withInteriorType(InteriorType.LEATHER)
                .withColor("beige")
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build();

        Assertions.assertEquals(InteriorType.LEATHER, interior.getInteriorType());
        Assertions.assertEquals("beige", interior.getColor());
        Assertions.assertEquals(Set.of(CAR_MODEL_ID), interior.getCompatibleCars());
    }

    @Test
    void steeringWheelBuilderShouldKeepCompatibleCarModels() {
        SteeringWheel steeringWheel = new SteeringWheelBuilder()
                .withId(UUID.randomUUID())
                .withName("M-Sport heated")
                .withPrice(new Money(25_000))
                .withType(SteeringWheelType.SPORTY)
                .withMaterial(SteeringWheelMaterial.LEATHER)
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build();

        Assertions.assertEquals(SteeringWheelMaterial.LEATHER, steeringWheel.getMaterial());
        Assertions.assertEquals(Set.of(CAR_MODEL_ID), steeringWheel.getCompatibleCars());
    }

    @Test
    void wheelsBuilderShouldKeepCompatibleCarModels() {
        Wheels wheels = new WheelsBuilder()
                .withId(UUID.randomUUID())
                .withName("19'' M-Sport")
                .withPrice(new Money(95_000))
                .withSeason(WheelSeason.SUMMER)
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build();

        Assertions.assertEquals(new Money(95_000), wheels.getPrice());
        Assertions.assertEquals(Set.of(CAR_MODEL_ID), wheels.getCompatibleCars());
    }

    @Test
    void buildersShouldRejectMissingValues() {
        Assertions.assertThrows(DomainValidationException.class, () -> new BodyBuilder().withId(null));
        Assertions.assertThrows(DomainValidationException.class, () -> new BodyBuilder().withName(null));
        Assertions.assertThrows(DomainValidationException.class, () -> new BodyBuilder().withPrice(null));
        Assertions.assertThrows(DomainValidationException.class, () -> new BodyBuilder().withBody(null));
        Assertions.assertThrows(DomainValidationException.class,
                () -> new BodyBuilder().withCompatibleCarModelId(null));

        Assertions.assertThrows(DomainValidationException.class, () -> new BodyBuilder()
                .withId(UUID.randomUUID())
                .withName("Body")
                .withPrice(Money.ZERO)
                .withBody(BodyType.SEDAN)
                .build());

        Assertions.assertThrows(DomainValidationException.class, () -> new WheelsBuilder()
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build());

        Assertions.assertThrows(DomainValidationException.class, () -> new EngineBuilder()
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build());

        Assertions.assertThrows(DomainValidationException.class, () -> new GearBoxBuilder()
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build());

        Assertions.assertThrows(DomainValidationException.class, () -> new InteriorBuilder()
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build());

        Assertions.assertThrows(DomainValidationException.class, () -> new SteeringWheelBuilder()
                .withCompatibleCarModelId(CAR_MODEL_ID)
                .build());
    }

    @Test
    void sparePartShouldNotExposeMutableCompatibleCarModels() {
        Body body = new Body(BodyType.SEDAN, UUID.randomUUID(), "Body", Money.ZERO, Set.of(CAR_MODEL_ID));

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> body.getCompatibleCars().add(ANOTHER_CAR_MODEL_ID));

        body.addCompatibleCar(ANOTHER_CAR_MODEL_ID);
        Assertions.assertEquals(Set.of(CAR_MODEL_ID, ANOTHER_CAR_MODEL_ID), body.getCompatibleCars());
    }
}
