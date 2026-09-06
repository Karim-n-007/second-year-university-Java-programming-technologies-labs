package ru.nursafin.presentation;

import ru.nursafin.application.filters.carModel.SuitableCars;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderCustomCarRepository.OrderCustomCarRepository;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderReadyCarModelRepository.OrderReadyCarRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.*;
import ru.nursafin.application.repositories.entitiesRepository.testDriveRepository.TestDriveCarRepository;
import ru.nursafin.application.repositories.entitiesRepository.testDriveRepository.TestDriveRequestRepository;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.application.services.carModelService.CarModelService;
import ru.nursafin.application.services.orderService.EmployeeAssignment;
import ru.nursafin.application.services.orderService.customCarModelOrderService.CustomCarModelOrderService;
import ru.nursafin.application.services.orderService.readyCarModelOrderService.ReadyCarModelOrderService;
import ru.nursafin.application.services.sparePartService.body.BodyService;
import ru.nursafin.application.services.sparePartService.engine.EngineService;
import ru.nursafin.application.services.sparePartService.gearbox.GearboxService;
import ru.nursafin.application.services.sparePartService.interior.InteriorService;
import ru.nursafin.application.services.sparePartService.steeringWheel.SteeringWheelService;
import ru.nursafin.application.services.sparePartService.wheels.WheelsService;
import ru.nursafin.application.services.testDriveService.TestDriveService;
import ru.nursafin.application.services.userService.clientService.ClientService;
import ru.nursafin.application.services.userService.employeeService.EmployeeService;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.carModelsDataStorage.InMemoryCarModelsDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.orderCustomCarModelDataStorage.InMemoryOrderCustomCarModelDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.orderReadyCarModelDataStorage.InMemoryOrderReadyCarModelDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.sparePartsDataStorage.*;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.testDriveDataStorage.InMemoryTestDriveCarDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.testDriveDataStorage.InMemoryTestDriveRequestDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.usersDataStorage.clientDataStorage.InMemoryClientDataStorage;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.usersDataStorage.employeeDataStorage.InMemoryEmployeeDataStorage;
import ru.nursafin.infrastructure.repositories.inMemoryCarModelRepository.InMemoryCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemoryOrderRepository.inMemoryOrderCustomCarModelRepository.InMemoryOrderCustomCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemoryOrderRepository.inMemoryOrderReadyCarModelRepository.InMemoryOrderReadyCarModelRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryBodyRepository.InMemoryBodyRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryEngineRepository.InMemoryEngineRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryGearboxRepository.InMemoryGearboxRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryInteriorRepository.InMemoryInteriorRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemorySteeringWheelRepository.InMemorySteeringWheelRepository;
import ru.nursafin.infrastructure.repositories.inMemorySparePartRepository.inMemoryWheelsRepository.InMemoryWheelsRepository;
import ru.nursafin.infrastructure.repositories.inMemoryTestDriveRepository.InMemoryTestDriveCarRepository;
import ru.nursafin.infrastructure.repositories.inMemoryTestDriveRepository.InMemoryTestDriveRequestRepository;
import ru.nursafin.infrastructure.repositories.inMemoryUsersRepository.clientRepository.InMemoryClientRepository;
import ru.nursafin.infrastructure.repositories.inMemoryUsersRepository.employeeRepository.InMemoryEmployeeRepository;

public class ApplicationContext {
    private final CarModelRepository carModelRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    private final CarModelService carModelService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final BodyService bodyService;
    private final EngineService engineService;
    private final GearboxService gearboxService;
    private final InteriorService interiorService;
    private final SteeringWheelService steeringWheelService;
    private final WheelsService wheelsService;
    private final ReadyCarModelOrderService readyCarModelOrderService;
    private final CustomCarModelOrderService customCarModelOrderService;
    private final TestDriveService testDriveService;
    private final SuitableCars suitableCars;

    public ApplicationContext() {
        InMemoryBodyDataStorage bodyDataStorage = new InMemoryBodyDataStorage();
        InMemoryEngineDataStorage engineDataStorage = new InMemoryEngineDataStorage();
        InMemoryGearboxDataStorage gearboxDataStorage = new InMemoryGearboxDataStorage();
        InMemoryInteriorDataStorage interiorDataStorage = new InMemoryInteriorDataStorage();
        InMemorySteeringWheelDataStorage steeringWheelDataStorage = new InMemorySteeringWheelDataStorage();
        InMemoryWheelsDataStorage wheelsDataStorage = new InMemoryWheelsDataStorage();

        BodySparePartRepository bodyRepository = new InMemoryBodyRepository(bodyDataStorage);
        EngineSparePartRepository engineRepository = new InMemoryEngineRepository(engineDataStorage);
        GearboxSparePartRepository gearboxRepository = new InMemoryGearboxRepository(gearboxDataStorage);
        InteriorSparePartRepository interiorRepository = new InMemoryInteriorRepository(interiorDataStorage);
        SteeringWheelSparePartRepository steeringWheelRepository = new InMemorySteeringWheelRepository(steeringWheelDataStorage);
        WheelsSparePartRepository wheelsRepository = new InMemoryWheelsRepository(wheelsDataStorage);

        carModelRepository = new InMemoryCarModelRepository(new InMemoryCarModelsDataStorage());
        clientRepository = new InMemoryClientRepository(new InMemoryClientDataStorage());
        employeeRepository = new InMemoryEmployeeRepository(new InMemoryEmployeeDataStorage());

        OrderReadyCarRepository orderReadyCarRepository =
                new InMemoryOrderReadyCarModelRepository(new InMemoryOrderReadyCarModelDataStorage());
        OrderCustomCarRepository orderCustomCarRepository =
                new InMemoryOrderCustomCarModelRepository(new InMemoryOrderCustomCarModelDataStorage());
        TestDriveRequestRepository testDriveRequestRepository =
                new InMemoryTestDriveRequestRepository(new InMemoryTestDriveRequestDataStorage());
        TestDriveCarRepository testDriveCarRepository =
                new InMemoryTestDriveCarRepository(new InMemoryTestDriveCarDataStorage());

        EmployeeAssignment employeeAssignment = new EmployeeAssignment(employeeRepository);

        bodyService = new BodyService(bodyRepository, carModelRepository);
        engineService = new EngineService(engineRepository, carModelRepository);
        gearboxService = new GearboxService(gearboxRepository, carModelRepository);
        interiorService = new InteriorService(interiorRepository, carModelRepository);
        steeringWheelService = new SteeringWheelService(steeringWheelRepository, carModelRepository);
        wheelsService = new WheelsService(wheelsRepository, carModelRepository);

        carModelService = new CarModelService(carModelRepository, bodyRepository, engineRepository, gearboxRepository,
                interiorRepository, steeringWheelRepository, wheelsRepository);
        clientService = new ClientService(clientRepository);
        employeeService = new EmployeeService(employeeRepository);

        readyCarModelOrderService = new ReadyCarModelOrderService(orderReadyCarRepository, clientRepository,
                carModelRepository, employeeAssignment);
        customCarModelOrderService = new CustomCarModelOrderService(orderCustomCarRepository, clientRepository,
                carModelRepository, employeeAssignment, bodyRepository, engineRepository, gearboxRepository,
                steeringWheelRepository, interiorRepository, wheelsRepository);
        testDriveService = new TestDriveService(testDriveRequestRepository, testDriveCarRepository,
                carModelRepository, clientRepository);

        suitableCars = new SuitableCars(carModelRepository);
    }

    public CarModelService getCarModelService() {
        return carModelService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public BodyService getBodyService() {
        return bodyService;
    }

    public EngineService getEngineService() {
        return engineService;
    }

    public GearboxService getGearboxService() {
        return gearboxService;
    }

    public InteriorService getInteriorService() {
        return interiorService;
    }

    public SteeringWheelService getSteeringWheelService() {
        return steeringWheelService;
    }

    public WheelsService getWheelsService() {
        return wheelsService;
    }

    public ReadyCarModelOrderService getReadyCarModelOrderService() {
        return readyCarModelOrderService;
    }

    public CustomCarModelOrderService getCustomCarModelOrderService() {
        return customCarModelOrderService;
    }

    public TestDriveService getTestDriveService() {
        return testDriveService;
    }

    public SuitableCars getSuitableCars() {
        return suitableCars;
    }
}
