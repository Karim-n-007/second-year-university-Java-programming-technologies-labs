package ru.nursafin.application.filters.carModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.body.BodyType;
import ru.nursafin.domainModel.entities.sparePart.engine.Drive;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.engine.FuelType;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.gearbox.GearboxType;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuitableCarsTest {
    @Mock
    CarModelRepository carModelRepository;

    @InjectMocks
    SuitableCars suitableCars;

    @Test
    void shouldReturnAllCars() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        CarModelFilter carModelFilter = new CarModelFilter();

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));


        List<CarModel> carModels = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(List.of(carModel1, carModel2, carModel3), carModels);
    }

    @Test
    void shouldFilterOneCarByBasePrice() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        Money basePriceCar1 = new Money(1000);
        Money basePriceCar2 = new Money(2000);
        Money basePriceCar3 = new Money(3000);

        Money minBasePriceFilter = new Money(500);
        Money maxBasePriceFilter = new Money(1500);

        when(carModel1.getBasePrice()).thenReturn(basePriceCar1);
        when(carModel2.getBasePrice()).thenReturn(basePriceCar2);
        when(carModel3.getBasePrice()).thenReturn(basePriceCar3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withMinBasePrice(minBasePriceFilter)
                .withMaxBasePrice(maxBasePriceFilter);


        List<CarModel> carModels = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(List.of(carModel1), carModels);
    }

    @Test
    void shouldFilterTwoCarsByBasePrice() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        Money basePriceCar1 = new Money(1000);
        Money basePriceCar2 = new Money(2000);
        Money basePriceCar3 = new Money(3000);

        Money minBasePriceFilter = new Money(500);
        Money maxBasePriceFilter = new Money(2500);

        when(carModel1.getBasePrice()).thenReturn(basePriceCar1);
        when(carModel2.getBasePrice()).thenReturn(basePriceCar2);
        when(carModel3.getBasePrice()).thenReturn(basePriceCar3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withMinBasePrice(minBasePriceFilter)
                .withMaxBasePrice(maxBasePriceFilter);


        List<CarModel> carModels = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(List.of(carModel1, carModel2), carModels);
    }

    @Test
    void shouldFilterOneCarByTotalPrice() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of (carModel1, carModel2, carModel3));

        Money filterMinTotalPrice = new Money(2000);
        Money filterMaxTotalPrice = new Money(3000);

        Money basePriceCar1 = new Money(1000);
        Money basePriceCar2 = new Money(2000);
        Money basePriceCar3 = new Money(3000);

        Money bodyPrice = new Money(100);
        Money enginePrice = new Money(100);
        Money gearboxPrice = new Money(100);
        Money interiorPrice = new Money(100);
        Money steeringWheelPrice = new Money(100);
        Money wheelsPrice = new Money(100);

        when(carModel1.getBasePrice()).thenReturn(basePriceCar1);
        when(carModel2.getBasePrice()).thenReturn(basePriceCar2);
        when(carModel3.getBasePrice()).thenReturn(basePriceCar3);

        Body body = mock(Body.class);
        Engine engine = mock(Engine.class);
        Gearbox gearbox = mock(Gearbox.class);
        Interior interior = mock(Interior.class);
        SteeringWheel steeringWheel = mock(SteeringWheel.class);
        Wheels wheels = mock(Wheels.class);

        when(body.getPrice()).thenReturn(bodyPrice);
        when(engine.getPrice()).thenReturn(enginePrice);
        when(gearbox.getPrice()).thenReturn(gearboxPrice);
        when(interior.getPrice()).thenReturn(interiorPrice);
        when(steeringWheel.getPrice()).thenReturn(steeringWheelPrice);
        when(wheels.getPrice()).thenReturn(wheelsPrice);

        when(carModel1.getBody()).thenReturn(body);
        when(carModel1.getEngine()).thenReturn(engine);
        when(carModel1.getGearbox()).thenReturn(gearbox);
        when(carModel1.getInterior()).thenReturn(interior);
        when(carModel1.getSteeringWheel()).thenReturn(steeringWheel);
        when(carModel1.getWheels()).thenReturn(wheels);

        when(carModel2.getBody()).thenReturn(body);
        when(carModel2.getEngine()).thenReturn(engine);
        when(carModel2.getGearbox()).thenReturn(gearbox);
        when(carModel2.getInterior()).thenReturn(interior);
        when(carModel2.getSteeringWheel()).thenReturn(steeringWheel);
        when(carModel2.getWheels()).thenReturn(wheels);

        when(carModel3.getBody()).thenReturn(body);
        when(carModel3.getEngine()).thenReturn(engine);
        when(carModel3.getGearbox()).thenReturn(gearbox);
        when(carModel3.getInterior()).thenReturn(interior);
        when(carModel3.getSteeringWheel()).thenReturn(steeringWheel);
        when(carModel3.getWheels()).thenReturn(wheels);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withMinTotalPrice(filterMinTotalPrice)
                .withMaxTotalPrice(filterMaxTotalPrice);

        List<CarModel> expected = List.of(carModel2);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterTwoCarByTotalPrice() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of (carModel1, carModel2, carModel3));

        Money filterMinTotalPrice = new Money(2000);
        Money filterMaxTotalPrice = new Money(4000);

        Money basePriceCar1 = new Money(1000);
        Money basePriceCar2 = new Money(2000);
        Money basePriceCar3 = new Money(3000);

        Money bodyPrice = new Money(100);
        Money enginePrice = new Money(100);
        Money gearboxPrice = new Money(100);
        Money interiorPrice = new Money(100);
        Money steeringWheelPrice = new Money(100);
        Money wheelsPrice = new Money(100);

        when(carModel1.getBasePrice()).thenReturn(basePriceCar1);
        when(carModel2.getBasePrice()).thenReturn(basePriceCar2);
        when(carModel3.getBasePrice()).thenReturn(basePriceCar3);

        Body body = mock(Body.class);
        Engine engine = mock(Engine.class);
        Gearbox gearbox = mock(Gearbox.class);
        Interior interior = mock(Interior.class);
        SteeringWheel steeringWheel = mock(SteeringWheel.class);
        Wheels wheels = mock(Wheels.class);

        when(body.getPrice()).thenReturn(bodyPrice);
        when(engine.getPrice()).thenReturn(enginePrice);
        when(gearbox.getPrice()).thenReturn(gearboxPrice);
        when(interior.getPrice()).thenReturn(interiorPrice);
        when(steeringWheel.getPrice()).thenReturn(steeringWheelPrice);
        when(wheels.getPrice()).thenReturn(wheelsPrice);

        when(carModel1.getBody()).thenReturn(body);
        when(carModel1.getEngine()).thenReturn(engine);
        when(carModel1.getGearbox()).thenReturn(gearbox);
        when(carModel1.getInterior()).thenReturn(interior);
        when(carModel1.getSteeringWheel()).thenReturn(steeringWheel);
        when(carModel1.getWheels()).thenReturn(wheels);

        when(carModel2.getBody()).thenReturn(body);
        when(carModel2.getEngine()).thenReturn(engine);
        when(carModel2.getGearbox()).thenReturn(gearbox);
        when(carModel2.getInterior()).thenReturn(interior);
        when(carModel2.getSteeringWheel()).thenReturn(steeringWheel);
        when(carModel2.getWheels()).thenReturn(wheels);

        when(carModel3.getBody()).thenReturn(body);
        when(carModel3.getEngine()).thenReturn(engine);
        when(carModel3.getGearbox()).thenReturn(gearbox);
        when(carModel3.getInterior()).thenReturn(interior);
        when(carModel3.getSteeringWheel()).thenReturn(steeringWheel);
        when(carModel3.getWheels()).thenReturn(wheels);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withMinTotalPrice(filterMinTotalPrice)
                .withMaxTotalPrice(filterMaxTotalPrice);

        List<CarModel> expected = List.of(carModel2, carModel3);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterOneCarByBrand() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        String brand1 = "BMW";
        String brand2 = "Honda";
        String brand3 = "Audi";

        when(carModel1.getBrand()).thenReturn(brand1);
        when(carModel2.getBrand()).thenReturn(brand2);
        when(carModel3.getBrand()).thenReturn(brand3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withBrand(brand1);

        List<CarModel> expected = List.of(carModel1);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterOneCarByBrandIfNameHaveDifferentRegister() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        String brand1 = "BmW";
        String brand2 = "HoNdA";
        String brand3 = "auDI";

        when(carModel1.getBrand()).thenReturn("BMW");
        when(carModel2.getBrand()).thenReturn("Honda");
        when(carModel3.getBrand()).thenReturn("Audi");

        CarModelFilter carModelFilter = new CarModelFilter()
                .withBrand(brand1);

        List<CarModel> expected = List.of(carModel1);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterOneCarByBodyType() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        Body body1 = mock(Body.class);
        Body body2 = mock(Body.class);
        Body body3 = mock(Body.class);

        BodyType bodyType1 = BodyType.HATCHBACK;
        BodyType bodyType2 = BodyType.PICKUP_TRUCK;
        BodyType bodyType3 = BodyType.CROSSOVER;

        when(body1.getBodyType()).thenReturn(bodyType1);
        when(body2.getBodyType()).thenReturn(bodyType2);
        when(body3.getBodyType()).thenReturn(bodyType3);

        when(carModel1.getBody()).thenReturn(body1);
        when(carModel2.getBody()).thenReturn(body2);
        when(carModel3.getBody()).thenReturn(body3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withBodyType(BodyType.HATCHBACK);

        List<CarModel> expected = List.of(carModel1);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterOneCarByFuelType() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        Engine engine1 = mock(Engine.class);
        Engine engine2 = mock(Engine.class);
        Engine engine3 = mock(Engine.class);

        FuelType fuelType1 = FuelType.ELECTRICITY;
        FuelType fuelType2 = FuelType.DIESEL;
        FuelType fuelType3 = FuelType.GASOLINE;

        when(engine1.getFuelType()).thenReturn(fuelType1);
        when(engine2.getFuelType()).thenReturn(fuelType2);
        when(engine3.getFuelType()).thenReturn(fuelType3);

        when(carModel1.getEngine()).thenReturn(engine1);
        when(carModel2.getEngine()).thenReturn(engine2);
        when(carModel3.getEngine()).thenReturn(engine3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withFuelType(fuelType3);

        List<CarModel> expected = List.of(carModel3);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterOneCarByGearboxType() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        Gearbox gearbox1 = mock(Gearbox.class);
        Gearbox gearbox2 = mock(Gearbox.class);
        Gearbox gearbox3 = mock(Gearbox.class);

        GearboxType gearboxType1 = GearboxType.AUTOMATIC;
        GearboxType gearboxType2 = GearboxType.MECHANICAL;
        GearboxType gearboxType3 = GearboxType.AUTOMATIC;

        when(gearbox1.getGearboxType()).thenReturn(gearboxType1);
        when(gearbox2.getGearboxType()).thenReturn(gearboxType2);
        when(gearbox3.getGearboxType()).thenReturn(gearboxType3);

        when(carModel1.getGearbox()).thenReturn(gearbox1);
        when(carModel2.getGearbox()).thenReturn(gearbox2);
        when(carModel3.getGearbox()).thenReturn(gearbox3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withGearboxType(gearboxType2);

        List<CarModel> expected = List.of(carModel2);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterTwoCarsByDrive() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        Drive drive1 = Drive.ALL_WHEEL_DRIVE;
        Drive drive2 = Drive.FRONT_WHEEL_DRIVE;
        Drive drive3 = Drive.FRONT_WHEEL_DRIVE;

        when(carModel1.getDrive()).thenReturn(drive1);
        when(carModel2.getDrive()).thenReturn(drive2);
        when(carModel3.getDrive()).thenReturn(drive3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withDrive(drive2);

        List<CarModel> expected = List.of(carModel2, carModel3);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterThreeCarsByInteriorColor() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);
        CarModel carModel4 = mock(CarModel.class);
        CarModel carModel5 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3, carModel4, carModel5));

        Interior interior1 = mock(Interior.class);
        Interior interior2 = mock(Interior.class);
        Interior interior3 = mock(Interior.class);
        Interior interior4 = mock(Interior.class);
        Interior interior5 = mock(Interior.class);

        String color1 = "White";
        String color2 = "Black";
        String color3 = "Blue";
        String color4 = "Black";
        String color5 = "Black";

        when(carModel1.getInterior()).thenReturn(interior1);
        when(carModel2.getInterior()).thenReturn(interior2);
        when(carModel3.getInterior()).thenReturn(interior3);
        when(carModel4.getInterior()).thenReturn(interior4);
        when(carModel5.getInterior()).thenReturn(interior5);

        when(interior1.getColor()).thenReturn(color1);
        when(interior2.getColor()).thenReturn(color2);
        when(interior3.getColor()).thenReturn(color3);
        when(interior4.getColor()).thenReturn(color4);
        when(interior5.getColor()).thenReturn(color5);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withInteriorColor("black");

        List<CarModel> expected = List.of(carModel2, carModel4, carModel5);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterOneCarByPower() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        Power powerCar1 = new Power(300);
        Power powerCar2 = new Power(400);
        Power powerCar3 = new Power(500);

        Engine engine1 = mock(Engine.class);
        Engine engine2 = mock(Engine.class);
        Engine engine3 = mock(Engine.class);

        when(engine1.getPower()).thenReturn(powerCar1);
        when(engine2.getPower()).thenReturn(powerCar2);
        when(engine3.getPower()).thenReturn(powerCar3);

        when(carModel1.getEngine()).thenReturn(engine1);
        when(carModel2.getEngine()).thenReturn(engine2);
        when(carModel3.getEngine()).thenReturn(engine3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withMinPower(new Power(250))
                .withMaxPower(new Power(350));

        List<CarModel> expected = List.of(carModel1);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterTwoCarByPower() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3));

        Power powerCar1 = new Power(300);
        Power powerCar2 = new Power(400);
        Power powerCar3 = new Power(500);

        Engine engine1 = mock(Engine.class);
        Engine engine2 = mock(Engine.class);
        Engine engine3 = mock(Engine.class);

        when(engine1.getPower()).thenReturn(powerCar1);
        when(engine2.getPower()).thenReturn(powerCar2);
        when(engine3.getPower()).thenReturn(powerCar3);

        when(carModel1.getEngine()).thenReturn(engine1);
        when(carModel2.getEngine()).thenReturn(engine2);
        when(carModel3.getEngine()).thenReturn(engine3);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withMinPower(new Power(350))
                .withMaxPower(new Power(550));

        List<CarModel> expected = List.of(carModel2, carModel3);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFilterOneCarWithSeveralDifferentFilters() {
        CarModel carModel1 = mock(CarModel.class);
        CarModel carModel2 = mock(CarModel.class);
        CarModel carModel3 = mock(CarModel.class);
        CarModel carModel4 = mock(CarModel.class);
        CarModel carModel5 = mock(CarModel.class);

        when(carModelRepository.findAll()).thenReturn(List.of(carModel1, carModel2, carModel3, carModel4, carModel5));

        Money basePrice1 = new Money(100000);
        Money basePrice2 = new Money(110000);
        Money basePrice3 = new Money(120000);
        Money basePrice4 = new Money(130000);
        Money basePrice5 = new Money(140000);

        Engine engine1 = mock(Engine.class);
        Engine engine4 = mock(Engine.class);

        Power powerCar1 = new Power(300);
        Power powerCar4 = new Power(600);

        String brand1 = "BMW";
        String brand2 = "Toyota";
        String brand3 = "Audi";
        String brand4 = "BMW";
        String brand5 = "Toyota";

        when(carModel1.getBrand()).thenReturn(brand1);
        when(carModel2.getBrand()).thenReturn(brand2);
        when(carModel3.getBrand()).thenReturn(brand3);
        when(carModel4.getBrand()).thenReturn(brand4);
        when(carModel5.getBrand()).thenReturn(brand5);

        when(carModel1.getEngine()).thenReturn(engine1);
        when(carModel4.getEngine()).thenReturn(engine4);

        when(carModel1.getBasePrice()).thenReturn(basePrice1);
        when(carModel2.getBasePrice()).thenReturn(basePrice2);
        when(carModel3.getBasePrice()).thenReturn(basePrice3);
        when(carModel4.getBasePrice()).thenReturn(basePrice4);
        when(carModel5.getBasePrice()).thenReturn(basePrice5);

        when(engine1.getPower()).thenReturn(powerCar1);
        when(engine4.getPower()).thenReturn(powerCar4);

        CarModelFilter carModelFilter = new CarModelFilter()
                .withBrand("BMW")
                .withMinBasePrice(new Money(50000))
                .withMaxBasePrice(new Money(150000))
                .withMinPower(new Power(500))
                .withMaxPower(new Power(1500));

        List<CarModel> expected = List.of(carModel4);


        List<CarModel> actual = suitableCars.GetSuitableCars(carModelFilter);


        Assertions.assertEquals(expected, actual);
    }
}
