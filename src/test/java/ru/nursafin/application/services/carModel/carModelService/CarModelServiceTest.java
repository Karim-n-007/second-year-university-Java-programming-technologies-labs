package ru.nursafin.application.services.carModel.carModelService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.*;
import ru.nursafin.application.services.carModelService.CarModelService;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Drive;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;


import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarModelServiceTest {
    @Mock
    private CarModelRepository carModelRepository;

    @Mock
    private BodySparePartRepository bodySparePartRepository;

    @Mock
    private EngineSparePartRepository engineSparePartRepository;

    @Mock
    private GearboxSparePartRepository gearboxSparePartRepository;

    @Mock
    private InteriorSparePartRepository interiorSparePartRepository;

    @Mock
    private SteeringWheelSparePartRepository steeringWheelSparePartRepository;

    @Mock
    private WheelsSparePartRepository wheelsSparePartRepository;

    @InjectMocks
    private CarModelService carModelService;


    @Test
    void shouldCreateAndSaveNewCarModelCorrectly() {
        String name = "Granta";
        String brand = "LADA";
        Money money = new Money(100500);
        Drive drive = Drive.ALL_WHEEL_DRIVE;

        UUID bodyId = UUID.randomUUID();
        UUID engineId = UUID.randomUUID();
        UUID gearboxId = UUID.randomUUID();
        UUID steeringWheelId = UUID.randomUUID();
        UUID interiorId = UUID.randomUUID();
        UUID wheelsId = UUID.randomUUID();

        Body body = mock(Body.class);
        Engine engine = mock(Engine.class);
        Gearbox gearbox = mock(Gearbox.class);
        SteeringWheel steeringWheel = mock(SteeringWheel.class);
        Interior interior = mock(Interior.class);
        Wheels wheels = mock(Wheels.class);

        when(bodySparePartRepository.findById(bodyId)).thenReturn(body);
        when(engineSparePartRepository.findById(engineId)).thenReturn(engine);
        when(gearboxSparePartRepository.findById(gearboxId)).thenReturn(gearbox);
        when(steeringWheelSparePartRepository.findById(steeringWheelId)).thenReturn(steeringWheel);
        when(interiorSparePartRepository.findById(interiorId)).thenReturn(interior);
        when(wheelsSparePartRepository.findById(wheelsId)).thenReturn(wheels);


        carModelService.createNewCarModel(name, brand, "white", money, drive, bodyId, engineId, gearboxId, steeringWheelId, interiorId, wheelsId);

        verify(bodySparePartRepository).findById(bodyId);
        verify(engineSparePartRepository).findById(engineId);
        verify(gearboxSparePartRepository).findById(gearboxId);
        verify(steeringWheelSparePartRepository).findById(steeringWheelId);
        verify(interiorSparePartRepository).findById(interiorId);
        verify(wheelsSparePartRepository).findById(wheelsId);

        verify(carModelRepository).save(any(CarModel.class));
    }

    @Test
    void shouldGetCarPriceCorrectly() {
        CarModel carModel = mock(CarModel.class);
        Body body = mock(Body.class);
        Engine engine = mock(Engine.class);
        Gearbox gearbox = mock(Gearbox.class);
        SteeringWheel steeringWheel = mock(SteeringWheel.class);
        Interior interior = mock(Interior.class);
        Wheels wheels = mock(Wheels.class);

        Money basePrice = new Money(100500);
        Money bodyPrice = new Money(100);
        Money enginePrice = new Money(200);
        Money gearboxPrice = new Money(300);
        Money steeringWheelPrice = new Money(400);
        Money interiorPrice = new Money(500);
        Money wheelsPrice = new Money(600);

        when(carModel.getBasePrice()).thenReturn(basePrice);
        when(body.getPrice()).thenReturn(bodyPrice);
        when(engine.getPrice()).thenReturn(enginePrice);
        when(gearbox.getPrice()).thenReturn(gearboxPrice);
        when(steeringWheel.getPrice()).thenReturn(steeringWheelPrice);
        when(interior.getPrice()).thenReturn(interiorPrice);
        when(wheels.getPrice()).thenReturn(wheelsPrice);

        when(carModel.getBody()).thenReturn(body);
        when(carModel.getEngine()).thenReturn(engine);
        when(carModel.getGearbox()).thenReturn(gearbox);
        when(carModel.getSteeringWheel()).thenReturn(steeringWheel);
        when(carModel.getInterior()).thenReturn(interior);
        when(carModel.getWheels()).thenReturn(wheels);


        Money expected = basePrice
                .plus(bodyPrice)
                .plus(enginePrice)
                .plus(gearboxPrice)
                .plus(steeringWheelPrice)
                .plus(interiorPrice)
                .plus(wheelsPrice);

        Money actual = carModelService.getModelCarPrice(carModel);

        Assertions.assertEquals(expected, actual);
    }
}
