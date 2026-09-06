package ru.nursafin.presentation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nursafin.application.filters.carModel.CarModelFilter;
import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.filters.testDrive.TestDriveFilter;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.order.OrderCustomCarModel;
import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;
import ru.nursafin.domainModel.entities.sparePart.SparePart;
import ru.nursafin.domainModel.entities.sparePart.body.BodyType;
import ru.nursafin.domainModel.entities.sparePart.engine.FuelType;
import ru.nursafin.domainModel.entities.sparePart.gearbox.GearboxType;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequestStatus;
import ru.nursafin.domainModel.entities.valueObjects.EngineDisplacement;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;
import ru.nursafin.domainModel.exceptions.DomainValidationException;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.domainModel.exceptions.IncompatibleComponentException;
import ru.nursafin.domainModel.statuses.CustomCarOrderStatus;
import ru.nursafin.domainModel.statuses.ReadyCarOrderStatus;
import ru.nursafin.domainModel.users.Employee.EmployeeRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CarDealershipScenarioTest {
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new ApplicationContext();
        new DemoDataInitializer(context).initialize();
    }

    @Test
    void shouldSeedCatalogFromSpecificationExample() {
        List<CarModel> carModels = context.getCarModelService().getAllCarModels();

        Assertions.assertEquals(3, carModels.size());
        Assertions.assertEquals(4, context.getEmployeeService().getAllEmployees().size());
        Assertions.assertEquals(2, context.getEmployeeService().getEmployeesByRole(EmployeeRole.SALES_MANAGER).size());
        Assertions.assertEquals(2, context.getClientService().getAllClients().size());
        Assertions.assertEquals(2, context.getTestDriveService().getCarsAvailableForTestDrive().size());

        CarModel bmw320i = findCar("BMW", "320i");
        Assertions.assertEquals(new Money(3_000_000), bmw320i.getBasePrice());
        Assertions.assertEquals(new Money(3_000_000), context.getCarModelService().getModelCarPrice(bmw320i));
    }

    @Test
    void shouldFilterCarsByEveryRequiredCriterion() {
        CarModel bmw320i = findCar("BMW", "320i");

        List<CarModel> byBrandAndModel = context.getSuitableCars().getSuitableCars(new CarModelFilter()
                .withBrand("bmw")
                .withModel("320i"));
        Assertions.assertEquals(List.of(bmw320i.getId()), byBrandAndModel.stream().map(CarModel::getId).toList());

        List<CarModel> byColor = context.getSuitableCars().getSuitableCars(new CarModelFilter().withColor("blue"));
        Assertions.assertEquals(1, byColor.size());
        Assertions.assertEquals("A4 Avant", byColor.get(0).getName());

        List<CarModel> byDisplacement = context.getSuitableCars().getSuitableCars(new CarModelFilter()
                .withMinEngineDisplacement(new EngineDisplacement(1996))
                .withMaxEngineDisplacement(new EngineDisplacement(2000)));
        Assertions.assertEquals(2, byDisplacement.size());

        List<CarModel> byPowerAndFuel = context.getSuitableCars().getSuitableCars(new CarModelFilter()
                .withFuelType(FuelType.DIESEL)
                .withMinPower(new Power(150))
                .withMaxPower(new Power(200)));
        Assertions.assertEquals(1, byPowerAndFuel.size());

        List<CarModel> byBodyAndGearbox = context.getSuitableCars().getSuitableCars(new CarModelFilter()
                .withBodyType(BodyType.SEDAN)
                .withGearboxType(GearboxType.AUTOMATIC));
        Assertions.assertEquals(2, byBodyAndGearbox.size());

        List<CarModel> byPrice = context.getSuitableCars().getSuitableCars(new CarModelFilter()
                .withMinBasePrice(new Money(3_100_000))
                .withMaxBasePrice(new Money(3_300_000)));
        Assertions.assertEquals(1, byPrice.size());
    }

    @Test
    void shouldRejectModelFilterWithoutBrand() {
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> context.getSuitableCars().getSuitableCars(new CarModelFilter().withModel("320i"))
        );
    }

    @Test
    void shouldCreateReadyCarOrderWithAutomaticallyAssignedManager() {
        UUID clientId = anyClientId();
        CarModel bmw320i = findCar("BMW", "320i");

        UUID orderId = context.getReadyCarModelOrderService().createOrder(clientId, bmw320i.getId());
        OrderReadyCarModel order = context.getReadyCarModelOrderService().findById(orderId);

        Assertions.assertEquals(clientId, order.getClientId());
        Assertions.assertEquals(ReadyCarOrderStatus.PLACED, order.getStatus());
        Assertions.assertTrue(context.getEmployeeService().getEmployeesByRole(EmployeeRole.SALES_MANAGER).stream()
                .anyMatch(employee -> employee.getId().equals(order.getEmployeeId())));

        context.getReadyCarModelOrderService().changeStatus(orderId, ReadyCarOrderStatus.APPROVED_BY_MANAGER);
        context.getReadyCarModelOrderService().changeStatus(orderId, ReadyCarOrderStatus.AWAITING_PAYMENT);

        Assertions.assertEquals(ReadyCarOrderStatus.AWAITING_PAYMENT,
                context.getReadyCarModelOrderService().findById(orderId).getStatus());
        Assertions.assertEquals(1, context.getReadyCarModelOrderService().findAll().size());
        Assertions.assertEquals(1, context.getReadyCarModelOrderService()
                .findAll(new OrderFilter().withClientId(clientId)).size());
    }

    @Test
    void shouldNotDuplicateOrderOnStatusChange() {
        UUID orderId = context.getReadyCarModelOrderService()
                .createOrder(anyClientId(), findCar("BMW", "320i").getId());

        context.getReadyCarModelOrderService().changeStatus(orderId, ReadyCarOrderStatus.APPROVED_BY_MANAGER);

        Assertions.assertEquals(1, context.getReadyCarModelOrderService().findAll().size());
    }

    @Test
    void shouldBuildValidConfigurationFromSpecificationExample() {
        UUID clientId = anyClientId();
        CarModel bmw320i = findCar("BMW", "320i");

        UUID orderId = context.getCustomCarModelOrderService().createOrder(
                clientId,
                bmw320i.getId(),
                bmw320i.getBody().getId(),
                bmw320i.getEngine().getId(),
                bmw320i.getGearbox().getId(),
                findSparePart(context.getSteeringWheelService().getAllSteeringWheels(), "M-Sport heated"),
                findSparePart(context.getInteriorService().getAllInteriors(), "Leather Dakota"),
                findSparePart(context.getWheelsService().getAllWheels(), "19'' M-Sport"));

        OrderCustomCarModel order = context.getCustomCarModelOrderService().findById(orderId);

        Assertions.assertEquals(new Money(3_230_000), order.getPriceAtCreateOrderMoment());
        Assertions.assertEquals(CustomCarOrderStatus.PLACED, order.getStatus());

        context.getCustomCarModelOrderService().changeStatus(orderId, CustomCarOrderStatus.APPROVED_BY_WAREHOUSE);
        Assertions.assertEquals(CustomCarOrderStatus.APPROVED_BY_WAREHOUSE,
                context.getCustomCarModelOrderService().findById(orderId).getStatus());
    }

    @Test
    void shouldApplyNegativeSurchargeForManualGearbox() {
        CarModel bmw320i = findCar("BMW", "320i");

        UUID orderId = context.getCustomCarModelOrderService().createOrder(
                anyClientId(),
                bmw320i.getId(),
                bmw320i.getBody().getId(),
                bmw320i.getEngine().getId(),
                findSparePart(context.getGearboxService().getAllGearboxes(), "Manual 6MT"),
                bmw320i.getSteeringWheel().getId(),
                bmw320i.getInterior().getId(),
                bmw320i.getWheels().getId());

        Assertions.assertEquals(new Money(2_970_000),
                context.getCustomCarModelOrderService().findById(orderId).getPriceAtCreateOrderMoment());
    }

    @Test
    void shouldRejectIncompatibleComponent() {
        CarModel bmw320i = findCar("BMW", "320i");
        UUID performanceInteriorId = findSparePart(context.getInteriorService().getAllInteriors(),
                "Sport Performance");

        Assertions.assertThrows(
                IncompatibleComponentException.class,
                () -> context.getCustomCarModelOrderService().createOrder(
                        anyClientId(),
                        bmw320i.getId(),
                        bmw320i.getBody().getId(),
                        bmw320i.getEngine().getId(),
                        bmw320i.getGearbox().getId(),
                        bmw320i.getSteeringWheel().getId(),
                        performanceInteriorId,
                        bmw320i.getWheels().getId())
        );
    }

    @Test
    void shouldRejectConfigurationWithoutRequiredNode() {
        CarModel bmw320i = findCar("BMW", "320i");

        DomainValidationException exception = Assertions.assertThrows(
                DomainValidationException.class,
                () -> context.getCustomCarModelOrderService().createOrder(
                        anyClientId(),
                        bmw320i.getId(),
                        bmw320i.getBody().getId(),
                        bmw320i.getEngine().getId(),
                        bmw320i.getGearbox().getId(),
                        bmw320i.getSteeringWheel().getId(),
                        null,
                        bmw320i.getWheels().getId())
        );

        Assertions.assertTrue(exception.getMessage().contains("Interior"));
    }

    @Test
    void shouldBookAndProcessTestDrive() {
        UUID clientId = anyClientId();
        CarModel bmw320i = findCar("BMW", "320i");
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(3).withNano(0);

        UUID requestId = context.getTestDriveService().requestTestDrive(clientId, bmw320i.getId(), startDateTime);

        Assertions.assertEquals(1, context.getTestDriveService()
                .findAll(new TestDriveFilter().withClientId(clientId)).size());

        context.getTestDriveService().changeStatus(requestId, TestDriveRequestStatus.CONFIRMED);
        Assertions.assertEquals(TestDriveRequestStatus.CONFIRMED,
                context.getTestDriveService().findById(requestId).getStatus());

        Assertions.assertThrows(
                DomainValidationException.class,
                () -> context.getTestDriveService().requestTestDrive(clientId, bmw320i.getId(), startDateTime)
        );

        context.getTestDriveService().deleteById(requestId);
        Assertions.assertTrue(context.getTestDriveService().findAll().isEmpty());
    }

    @Test
    void shouldRejectTestDriveForCarOutsideTestDriveList() {
        CarModel audi = findCar("Audi", "A4 Avant");

        Assertions.assertThrows(
                DomainValidationException.class,
                () -> context.getTestDriveService()
                        .requestTestDrive(anyClientId(), audi.getId(), LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void shouldManageTestDriveCarListByManager() {
        CarModel audi = findCar("Audi", "A4 Avant");

        context.getTestDriveService().addCarToTestDriveList(audi.getId());
        Assertions.assertEquals(3, context.getTestDriveService().getCarsAvailableForTestDrive().size());

        context.getTestDriveService().removeCarFromTestDriveList(audi.getId());
        Assertions.assertEquals(2, context.getTestDriveService().getCarsAvailableForTestDrive().size());
    }

    @Test
    void shouldPersistSparePartPriceUpdate() {
        UUID wheelsId = findSparePart(context.getWheelsService().getAllWheels(), "19'' M-Sport");

        context.getWheelsService().updateWheelsPrice(wheelsId, new Money(120_000));

        Assertions.assertEquals(new Money(120_000), context.getWheelsService().findById(wheelsId).getPrice());
    }

    @Test
    void shouldPersistCompatibilityUpdate() {
        CarModel audi = findCar("Audi", "A4 Avant");
        UUID sportWheelsId = findSparePart(context.getWheelsService().getAllWheels(), "19'' M-Sport");

        Assertions.assertFalse(context.getWheelsService().findById(sportWheelsId).getCompatibleCars()
                .contains(audi.getId()));

        context.getWheelsService().addNewCarCompatibleWithWheels(sportWheelsId, audi.getId());

        Assertions.assertTrue(context.getWheelsService().findById(sportWheelsId).getCompatibleCars()
                .contains(audi.getId()));
    }

    @Test
    void shouldUpdateCarModelByWarehouseAdministrator() {
        CarModel bmw320i = findCar("BMW", "320i");

        context.getCarModelService().updateCarModel(bmw320i.getId(), "320i", "BMW", "black",
                new Money(3_100_000), bmw320i.getDrive(), bmw320i.getBody().getId(), bmw320i.getEngine().getId(),
                bmw320i.getGearbox().getId(), bmw320i.getSteeringWheel().getId(), bmw320i.getInterior().getId(),
                bmw320i.getWheels().getId());

        CarModel updated = context.getCarModelService().findById(bmw320i.getId());

        Assertions.assertEquals("black", updated.getColor());
        Assertions.assertEquals(new Money(3_100_000), updated.getBasePrice());
        Assertions.assertEquals(3, context.getCarModelService().getAllCarModels().size());
    }

    @Test
    void shouldDeleteEntitiesBySystemAdministrator() {
        CarModel audi = findCar("Audi", "A4 Avant");

        context.getCarModelService().deleteById(audi.getId());
        Assertions.assertEquals(2, context.getCarModelService().getAllCarModels().size());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getCarModelService().findById(audi.getId()));

        UUID clientId = anyClientId();
        context.getClientService().deleteById(clientId);
        Assertions.assertEquals(1, context.getClientService().getAllClients().size());

        UUID employeeId = context.getEmployeeService().getAllEmployees().get(0).getId();
        context.getEmployeeService().deleteById(employeeId);
        Assertions.assertEquals(3, context.getEmployeeService().getAllEmployees().size());
    }

    @Test
    void shouldThrowNotFoundExceptionForUnknownIdentifiers() {
        UUID unknownId = UUID.randomUUID();

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getCarModelService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getClientService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getEmployeeService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getTestDriveService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getReadyCarModelOrderService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getCustomCarModelOrderService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getBodyService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getEngineService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getGearboxService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getInteriorService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getSteeringWheelService().findById(unknownId));
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> context.getWheelsService().findById(unknownId));
    }

    @Test
    void shouldDeleteSparePartsAndOrders() {
        UUID aeroWheelsId = findSparePart(context.getWheelsService().getAllWheels(), "18'' Aero");
        context.getWheelsService().deleteById(aeroWheelsId);
        Assertions.assertEquals(2, context.getWheelsService().getAllWheels().size());

        UUID readyOrderId = context.getReadyCarModelOrderService()
                .createOrder(anyClientId(), findCar("BMW", "330i").getId());
        context.getReadyCarModelOrderService().deleteById(readyOrderId);
        Assertions.assertTrue(context.getReadyCarModelOrderService().findAll().isEmpty());

        CarModel bmw330i = findCar("BMW", "330i");
        UUID customOrderId = context.getCustomCarModelOrderService().createOrder(anyClientId(), bmw330i.getId(),
                bmw330i.getBody().getId(), bmw330i.getEngine().getId(), bmw330i.getGearbox().getId(),
                bmw330i.getSteeringWheel().getId(), bmw330i.getInterior().getId(), bmw330i.getWheels().getId());
        context.getCustomCarModelOrderService().deleteById(customOrderId);
        Assertions.assertTrue(context.getCustomCarModelOrderService().findAll().isEmpty());
    }

    private CarModel findCar(String brand, String model) {
        return context.getCarModelService().getAllCarModels().stream()
                .filter(carModel -> carModel.getBrand().equals(brand) && carModel.getName().equals(model))
                .findFirst()
                .orElseThrow();
    }

    private UUID findSparePart(List<? extends SparePart> spareParts, String name) {
        return spareParts.stream()
                .filter(sparePart -> sparePart.getName().equals(name))
                .map(SparePart::getId)
                .findFirst()
                .orElseThrow();
    }

    private UUID anyClientId() {
        return context.getClientService().getAllClients().get(0).getId();
    }
}
