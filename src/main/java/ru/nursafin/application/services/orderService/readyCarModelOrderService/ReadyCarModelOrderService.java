package ru.nursafin.application.services.orderService.readyCarModelOrderService;

import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderReadyCarModelRepository.OrderReadyCarRepository;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.services.orderService.EmployeeAssignment;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;
import ru.nursafin.domainModel.statuses.ReadyCarOrderStatus;
import ru.nursafin.domainModel.users.Employee.Employee;

import java.util.List;
import java.util.UUID;

public class ReadyCarModelOrderService {
    private final OrderReadyCarRepository orderReadyCarRepository;
    private final ClientRepository clientRepository;
    private final CarModelRepository carModelRepository;
    private final EmployeeAssignment employeeAssignment;

    public ReadyCarModelOrderService(OrderReadyCarRepository orderReadyCarRepository,
                                     ClientRepository clientRepository,
                                     CarModelRepository carModelRepository,
                                     EmployeeAssignment employeeAssignment) {
        this.orderReadyCarRepository = orderReadyCarRepository;
        this.clientRepository = clientRepository;
        this.carModelRepository = carModelRepository;
        this.employeeAssignment = employeeAssignment;
    }

    public UUID createOrder(UUID clientId, UUID carModelId) {
        if (clientId == null) {
            throw new DomainValidationException("missing required field \"client\"");
        }
        if (carModelId == null) {
            throw new DomainValidationException("missing required field \"car model\"");
        }

        clientRepository.findById(clientId);
        CarModel carModel = carModelRepository.findById(carModelId);
        Employee employee = employeeAssignment.assignSalesManager();

        Money carPrice = calculateReadyCarPrice(carModel);

        OrderReadyCarModel order = new OrderReadyCarModel(
                UUID.randomUUID(), clientId, employee.getId(), carModel, carPrice);

        return orderReadyCarRepository.save(order);
    }

    public OrderReadyCarModel findById(UUID orderId) {
        return orderReadyCarRepository.findById(orderId);
    }

    public List<OrderReadyCarModel> findAll(OrderFilter filter) {
        return orderReadyCarRepository.findAll().stream()
                .filter(order -> filter.getEmployeeId() == null ||
                        order.getEmployeeId().equals(filter.getEmployeeId()))
                .filter(order -> filter.getClientId() == null ||
                        order.getClientId().equals(filter.getClientId()))
                .toList();
    }

    public List<OrderReadyCarModel> findAll() {
        return orderReadyCarRepository.findAll();
    }

    public void changeStatus(UUID orderId, ReadyCarOrderStatus newStatus) {
        OrderReadyCarModel order = orderReadyCarRepository.findById(orderId);
        order.changeStatus(newStatus);

        orderReadyCarRepository.save(order);
    }

    public void deleteById(UUID orderId) {
        orderReadyCarRepository.findById(orderId);

        orderReadyCarRepository.deleteById(orderId);
    }

    public Money calculateReadyCarPrice(CarModel carModel) {
        Money price = carModel.getBasePrice()
                .plus(carModel.getBody().getPrice())
                .plus(carModel.getEngine().getPrice())
                .plus(carModel.getGearbox().getPrice())
                .plus(carModel.getSteeringWheel().getPrice())
                .plus(carModel.getInterior().getPrice())
                .plus(carModel.getWheels().getPrice());

        if (price.isNegative()) {
            throw new DomainValidationException("Car price cannot be negative");
        }

        return price;
    }
}
