package ru.nursafin.application.services.orderService.customCarModelOrderService;

import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderCustomCarRepository.OrderCustomCarRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.*;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.order.OrderCustomCarModel;
import ru.nursafin.domainModel.entities.sparePart.SparePart;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.IncompatibleComponentException;

import java.util.List;
import java.util.UUID;

public class CustomCarModelOrderService {
    private final OrderCustomCarRepository orderCustomCarRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final CarModelRepository carModelRepository;

    private final BodySparePartRepository bodySparePartRepository;
    private final EngineSparePartRepository engineSparePartRepository;
    private final GearboxSparePartRepository gearboxSparePartRepository;
    private final SteeringWheelSparePartRepository steeringWheelSparePartRepository;
    private final InteriorSparePartRepository interiorSparePartRepository;
    private final WheelsSparePartRepository wheelsSparePartRepository;

    public CustomCarModelOrderService(OrderCustomCarRepository orderCustomCarRepository, EmployeeRepository employeeRepository, ClientRepository clientRepository, CarModelRepository carModelRepository, BodySparePartRepository bodySparePartRepository, EngineSparePartRepository engineSparePartRepository, GearboxSparePartRepository gearboxSparePartRepository, SteeringWheelSparePartRepository steeringWheelSparePartRepository, InteriorSparePartRepository interiorSparePartRepository, WheelsSparePartRepository wheelsSparePartRepository) {
        this.orderCustomCarRepository = orderCustomCarRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.carModelRepository = carModelRepository;
        this.bodySparePartRepository = bodySparePartRepository;
        this.engineSparePartRepository = engineSparePartRepository;
        this.gearboxSparePartRepository = gearboxSparePartRepository;
        this.steeringWheelSparePartRepository = steeringWheelSparePartRepository;
        this.interiorSparePartRepository = interiorSparePartRepository;
        this.wheelsSparePartRepository = wheelsSparePartRepository;
    }

    public UUID createOrder(UUID employeeId, UUID clientId, UUID carModelId, UUID bodyId, UUID engineId,
                            UUID gearboxId, UUID steeringWheelId, UUID interiorId, UUID wheelsId) {
        clientRepository.findById(clientId);
        employeeRepository.findById(employeeId);

        CarModel carModel = carModelRepository.findById(carModelId);
        Body body = bodySparePartRepository.findById(bodyId);
        Engine engine = engineSparePartRepository.findById(engineId);
        Interior interior = interiorSparePartRepository.findById(interiorId);
        Wheels wheels = wheelsSparePartRepository.findById(wheelsId);
        SteeringWheel steeringWheel = steeringWheelSparePartRepository.findById(steeringWheelId);
        Gearbox gearbox = gearboxSparePartRepository.findById(gearboxId);

        checkCompatibility(body, carModel);
        checkCompatibility(engine, carModel);
        checkCompatibility(interior, carModel);
        checkCompatibility(wheels, carModel);
        checkCompatibility(steeringWheel, carModel);
        checkCompatibility(gearbox, carModel);

        Money customCarPrice = carModel.getBasePrice()
                .plus(body.getPrice())
                .plus(engine.getPrice())
                .plus(interior.getPrice())
                .plus(wheels.getPrice())
                .plus(steeringWheel.getPrice())
                .plus(gearbox.getPrice());

        OrderCustomCarModel orderCustomCarModel = new OrderCustomCarModel(clientId, employeeId, carModel, body, engine, gearbox, steeringWheel, interior, wheels, customCarPrice);

        return orderCustomCarRepository.save(orderCustomCarModel);
    }

    public List<OrderCustomCarModel> findAll(OrderFilter filter) {
        return orderCustomCarRepository.findAll().stream()
                .filter(order -> filter.getEmployeeId() == null ||
                        order.getEmployeeId().equals(filter.getEmployeeId()))
                .filter(order -> filter.getClientId() == null ||
                        order.getClientId().equals(filter.getClientId()))
                .toList();
    }

    public List<OrderCustomCarModel> findAll() {
        return orderCustomCarRepository.findAll();
    }

    private void checkCompatibility(SparePart sparePart, CarModel carModel) {
        if (!sparePart.getCompatibleCars().contains(carModel.getId())) {
            throw new IncompatibleComponentException("Spare part: " +sparePart.getId() + "is not compatible with this car model: " + carModel.getId());
        }
    }
}
