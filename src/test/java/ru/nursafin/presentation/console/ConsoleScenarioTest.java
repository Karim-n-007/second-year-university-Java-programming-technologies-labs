package ru.nursafin.presentation.console;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.sparePart.SparePart;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequestStatus;
import ru.nursafin.domainModel.statuses.ReadyCarOrderStatus;
import ru.nursafin.presentation.ApplicationContext;
import ru.nursafin.presentation.DemoDataInitializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ConsoleScenarioTest {
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new ApplicationContext();
        new DemoDataInitializer(context).initialize();
    }

    @Test
    void clientShouldSeeFilteredCarsAndCarCard() {
        CarModel bmw320i = findCar("BMW", "320i");

        String output = run("""
                1
                1


                BMW
                320i










                2
                %s
                0
                0
                """.formatted(bmw320i.getId()));

        Assertions.assertTrue(output.contains("BMW 320i"));
        Assertions.assertTrue(output.contains("Base price: 3000000.00"));
        Assertions.assertFalse(output.contains("A4 Avant"));
    }

    @Test
    void clientShouldCreateReadyCarOrder() {
        CarModel bmw320i = findCar("BMW", "320i");
        UUID clientId = anyClientId();

        String output = run("""
                1
                3
                %s
                %s
                0
                0
                """.formatted(clientId, bmw320i.getId()));

        Assertions.assertTrue(output.contains("Order created"));
        Assertions.assertEquals(1, context.getReadyCarModelOrderService().findAll().size());
        Assertions.assertEquals(clientId, context.getReadyCarModelOrderService().findAll().get(0).getClientId());
    }

    @Test
    void clientShouldCreateCustomCarOrder() {
        CarModel bmw320i = findCar("BMW", "320i");
        UUID clientId = anyClientId();

        String output = run("""
                1
                4
                %s
                %s
                %s
                %s
                %s
                %s
                %s
                %s
                0
                0
                """.formatted(clientId, bmw320i.getId(), bmw320i.getBody().getId(), bmw320i.getEngine().getId(),
                bmw320i.getGearbox().getId(),
                findSparePart(context.getSteeringWheelService().getAllSteeringWheels(), "M-Sport heated"),
                findSparePart(context.getInteriorService().getAllInteriors(), "Leather Dakota"),
                findSparePart(context.getWheelsService().getAllWheels(), "19'' M-Sport")));

        Assertions.assertTrue(output.contains("Order created"));
        Assertions.assertTrue(output.contains("3230000.00"));
        Assertions.assertEquals(1, context.getCustomCarModelOrderService().findAll().size());
    }

    @Test
    void clientShouldBookTestDriveAndSeeOwnRequests() {
        CarModel bmw320i = findCar("BMW", "320i");
        UUID clientId = anyClientId();
        String startDateTime = LocalDateTime.now().plusDays(2).withHour(11).withMinute(30)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        String output = run("""
                1
                5
                %s
                %s
                %s
                7
                %s
                0
                0
                """.formatted(clientId, bmw320i.getId(), startDateTime, clientId));

        Assertions.assertTrue(output.contains("Test drive request created"));
        Assertions.assertEquals(1, context.getTestDriveService().findAll().size());
        Assertions.assertTrue(output.contains(TestDriveRequestStatus.REQUESTED.name()));
    }

    @Test
    void clientShouldSeeOwnOrders() {
        UUID clientId = anyClientId();
        context.getReadyCarModelOrderService().createOrder(clientId, findCar("BMW", "320i").getId());

        String output = run("""
                1
                6
                %s
                0
                0
                """.formatted(clientId));

        Assertions.assertTrue(output.contains("Orders for cars in stock:"));
        Assertions.assertTrue(output.contains(ReadyCarOrderStatus.PLACED.name()));
    }

    @Test
    void managerShouldChangeOrderStatusAndManageTestDriveList() {
        UUID clientId = anyClientId();
        UUID orderId = context.getReadyCarModelOrderService().createOrder(clientId, findCar("BMW", "320i").getId());
        CarModel audi = findCar("Audi", "A4 Avant");

        String output = run("""
                2
                5
                %s
                APPROVED_BY_MANAGER
                8
                %s
                7
                3


                0
                0
                """.formatted(orderId, audi.getId()));

        Assertions.assertTrue(output.contains("Status updated"));
        Assertions.assertEquals(ReadyCarOrderStatus.APPROVED_BY_MANAGER,
                context.getReadyCarModelOrderService().findById(orderId).getStatus());
        Assertions.assertTrue(output.contains("Car added to the test drive list"));
        Assertions.assertEquals(3, context.getTestDriveService().getCarsAvailableForTestDrive().size());
    }

    @Test
    void managerShouldSeeTestDriveRequestsAndChangeTheirStatus() {
        UUID clientId = anyClientId();
        UUID requestId = context.getTestDriveService()
                .requestTestDrive(clientId, findCar("BMW", "320i").getId(), LocalDateTime.now().plusDays(1));

        String output = run("""
                2
                10



                11
                %s
                CONFIRMED
                0
                0
                """.formatted(requestId));

        Assertions.assertTrue(output.contains("Test drive request status updated"));
        Assertions.assertEquals(TestDriveRequestStatus.CONFIRMED,
                context.getTestDriveService().findById(requestId).getStatus());
    }

    @Test
    void managerShouldSeeOrdersLists() {
        context.getReadyCarModelOrderService().createOrder(anyClientId(), findCar("BMW", "320i").getId());

        String output = run("""
                2
                3


                4


                9
                %s
                0
                0
                """.formatted(findCar("BMW", "330i").getId()));

        Assertions.assertTrue(output.contains(ReadyCarOrderStatus.PLACED.name()));
        Assertions.assertTrue(output.contains("Car removed from the test drive list"));
        Assertions.assertEquals(1, context.getTestDriveService().getCarsAvailableForTestDrive().size());
    }

    @Test
    void warehouseAdministratorShouldAddSparePartAndCar() {
        String output = run("""
                3
                6
                WHEELS
                20'' Sport
                150000
                SUMMER
                5
                1
                0
                0
                """);

        Assertions.assertTrue(output.contains("Spare part added"));
        Assertions.assertEquals(4, context.getWheelsService().getAllWheels().size());
        Assertions.assertTrue(output.contains("Wheels: "));
    }

    @Test
    void warehouseAdministratorShouldUpdateSparePartPriceAndCompatibility() {
        UUID wheelsId = findSparePart(context.getWheelsService().getAllWheels(), "18'' Aero");
        CarModel audi = findCar("Audi", "A4 Avant");

        String output = run("""
                3
                7
                WHEELS
                %s
                70000
                8
                WHEELS
                %s
                %s
                0
                0
                """.formatted(wheelsId, wheelsId, audi.getId()));

        Assertions.assertTrue(output.contains("Price updated"));
        Assertions.assertTrue(output.contains("Compatibility updated"));
        Assertions.assertEquals(new ru.nursafin.domainModel.entities.valueObjects.Money(70_000),
                context.getWheelsService().findById(wheelsId).getPrice());
        Assertions.assertTrue(context.getWheelsService().findById(wheelsId).getCompatibleCars()
                .contains(audi.getId()));
    }

    @Test
    void warehouseAdministratorShouldCreateAndUpdateCarModel() {
        CarModel bmw320i = findCar("BMW", "320i");

        String output = run("""
                3
                3
                Vesta
                LADA
                grey
                1500000
                FRONT_WHEEL_DRIVE
                %s
                %s
                %s
                %s
                %s
                %s
                4
                %s
                320i
                BMW
                blue
                3100000
                REAR_WHEEL_DRIVE
                %s
                %s
                %s
                %s
                %s
                %s
                0
                0
                """.formatted(bmw320i.getBody().getId(), bmw320i.getEngine().getId(), bmw320i.getGearbox().getId(),
                bmw320i.getSteeringWheel().getId(), bmw320i.getInterior().getId(), bmw320i.getWheels().getId(),
                bmw320i.getId(), bmw320i.getBody().getId(), bmw320i.getEngine().getId(), bmw320i.getGearbox().getId(),
                bmw320i.getSteeringWheel().getId(), bmw320i.getInterior().getId(), bmw320i.getWheels().getId()));

        Assertions.assertTrue(output.contains("Car added"));
        Assertions.assertTrue(output.contains("Car information updated"));
        Assertions.assertEquals(4, context.getCarModelService().getAllCarModels().size());
        Assertions.assertEquals("blue", context.getCarModelService().findById(bmw320i.getId()).getColor());
    }

    @Test
    void systemAdministratorShouldManageUsers() {
        UUID clientId = anyClientId();
        UUID employeeId = context.getEmployeeService().getAllEmployees().get(0).getId();

        String output = run("""
                4
                2
                New Client
                01.01.2000
                +79990000003
                new@example.com
                3
                %s
                Updated Client
                02.02.1999
                +79990000004
                changed@example.com
                6
                New Employee
                SALES_MANAGER
                7
                %s
                Updated Employee
                WAREHOUSE_ADMINISTRATOR
                1
                5
                0
                0
                """.formatted(clientId, employeeId));

        Assertions.assertTrue(output.contains("Client created"));
        Assertions.assertTrue(output.contains("Client updated"));
        Assertions.assertTrue(output.contains("Employee created"));
        Assertions.assertTrue(output.contains("Employee updated"));
        Assertions.assertEquals(3, context.getClientService().getAllClients().size());
        Assertions.assertEquals(5, context.getEmployeeService().getAllEmployees().size());
        Assertions.assertEquals("Updated Client", context.getClientService().findById(clientId).getName());
    }

    @Test
    void systemAdministratorShouldDeleteEntities() {
        UUID clientId = anyClientId();
        UUID employeeId = context.getEmployeeService().getAllEmployees().get(0).getId();
        UUID orderId = context.getReadyCarModelOrderService()
                .createOrder(clientId, findCar("BMW", "320i").getId());
        UUID requestId = context.getTestDriveService()
                .requestTestDrive(clientId, findCar("BMW", "330i").getId(), LocalDateTime.now().plusDays(1));

        String output = run("""
                4
                10
                %s
                12
                %s
                9
                %s
                8
                %s
                4
                %s
                0
                0
                """.formatted(orderId, requestId, findCar("Audi", "A4 Avant").getId(), employeeId, clientId));

        Assertions.assertTrue(output.contains("Order deleted"));
        Assertions.assertTrue(output.contains("Test drive request deleted"));
        Assertions.assertTrue(output.contains("Car deleted"));
        Assertions.assertTrue(output.contains("Employee deleted"));
        Assertions.assertTrue(output.contains("Client deleted"));
        Assertions.assertTrue(context.getReadyCarModelOrderService().findAll().isEmpty());
        Assertions.assertEquals(2, context.getCarModelService().getAllCarModels().size());
    }

    @Test
    void shouldReportDomainErrorsWithoutCrashing() {
        String output = run("""
                1
                3
                %s
                %s
                0
                0
                """.formatted(UUID.randomUUID(), UUID.randomUUID()));

        Assertions.assertTrue(output.contains("Error: "));
        Assertions.assertTrue(context.getReadyCarModelOrderService().findAll().isEmpty());
    }

    @Test
    void shouldReportInvalidInput() {
        String output = run("""
                1
                2
                not-a-uuid
                42
                0
                0
                """);

        Assertions.assertTrue(output.contains("Error: Invalid identifier: not-a-uuid"));
        Assertions.assertTrue(output.contains("Unknown menu item: 42"));
    }

    @Test
    void shouldRejectIncompatibleConfigurationFromConsole() {
        CarModel bmw320i = findCar("BMW", "320i");

        String output = run("""
                1
                4
                %s
                %s
                %s
                %s
                %s
                %s
                %s
                %s
                0
                0
                """.formatted(anyClientId(), bmw320i.getId(), bmw320i.getBody().getId(), bmw320i.getEngine().getId(),
                bmw320i.getGearbox().getId(), bmw320i.getSteeringWheel().getId(),
                findSparePart(context.getInteriorService().getAllInteriors(), "Sport Performance"),
                bmw320i.getWheels().getId()));

        Assertions.assertTrue(output.contains("Error: "));
        Assertions.assertTrue(context.getCustomCarModelOrderService().findAll().isEmpty());
    }

    @Test
    void shouldPrintMainMenuAndExitOnEmptyInput() {
        String output = run("");

        Assertions.assertTrue(output.contains("=== Car dealership ==="));
        Assertions.assertTrue(output.contains("Sign in as client"));
        Assertions.assertTrue(output.contains("Sign in as sales manager"));
        Assertions.assertTrue(output.contains("Sign in as warehouse administrator"));
        Assertions.assertTrue(output.contains("Sign in as system administrator"));
    }

    private String run(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ConsoleIo io = new ConsoleIo(inputStream, new PrintStream(outputStream, true, StandardCharsets.UTF_8));
        new MainMenu(io, context).run();

        return outputStream.toString(StandardCharsets.UTF_8);
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
