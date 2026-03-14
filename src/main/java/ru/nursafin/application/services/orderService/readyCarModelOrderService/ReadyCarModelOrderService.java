package ru.nursafin.application.services.orderService.readyCarModelOrderService;

import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderReadyCarModelRepository.OrderReadyCarRepository;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.statuses.CarPurchaseOrderStatus;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.domainModel.users.client.Client;

import java.util.List;
import java.util.UUID;

public class ReadyCarModelOrderService {
    private final OrderReadyCarRepository orderReadyCarRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final CarModelRepository carModelRepository;

    public ReadyCarModelOrderService(OrderReadyCarRepository orderReadyCarRepository, ClientRepository clientRepository, EmployeeRepository employeeRepository, CarModelRepository carModelRepository) {
        this.orderReadyCarRepository = orderReadyCarRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.carModelRepository = carModelRepository;
    }

    public UUID createOrder(UUID clientId, UUID employeeId, UUID carModelId) {
        Client client = clientRepository.findById(clientId);
        Employee employee = employeeRepository.findById(employeeId);
        CarModel carModel = carModelRepository.findById(carModelId);

        Money carPrice = calculateReadyCarPrice(carModel);

        OrderReadyCarModel order = new OrderReadyCarModel(client, employee, carModel, carPrice);

        return orderReadyCarRepository.save(order);
    }

    public List<OrderReadyCarModel> findAll(OrderFilter filter) {
        return orderReadyCarRepository.findAll().stream()
                .filter(order -> filter.getEmployeeId() == null ||
                        order.getEmployee().getId().equals(filter.getEmployeeId()))
                .filter(order -> filter.getClientId() == null ||
                        order.getClient().getId().equals(filter.getClientId()))
                .toList();
    }

    public List<OrderReadyCarModel> findAll() {
        return orderReadyCarRepository.findAll();
    }

    public void setStatus(CarPurchaseOrderStatus status, UUID orderId) {
        OrderReadyCarModel orderReadyCarModel = orderReadyCarRepository.findById(orderId);
        orderReadyCarModel.setStatus(status);
    }

    private Money calculateReadyCarPrice(CarModel carModel) {
        return carModel.getBasePrice()
                .plus(carModel.getBody().getPrice())
                .plus(carModel.getEngine().getPrice())
                .plus(carModel.getGearbox().getPrice())
                .plus(carModel.getSteeringWheel().getPrice())
                .plus(carModel.getInterior().getPrice())
                .plus(carModel.getWheels().getPrice());
    }
}
