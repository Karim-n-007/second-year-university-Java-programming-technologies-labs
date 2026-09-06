package ru.nursafin.application.services.orders.readyCarModeOrder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderReadyCarModelRepository.OrderReadyCarRepository;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.services.orderService.EmployeeAssignment;
import ru.nursafin.application.services.orderService.readyCarModelOrderService.ReadyCarModelOrderService;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;
import ru.nursafin.domainModel.statuses.ReadyCarOrderStatus;
import ru.nursafin.domainModel.users.Employee.Employee;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadyCarModelOrderServiceTest {
    @Mock
    private OrderReadyCarRepository orderReadyCarRepository;

    @Mock
    ClientRepository clientRepository;

    @Mock
    CarModelRepository carModelRepository;

    @Mock
    EmployeeAssignment employeeAssignment;

    @InjectMocks
    ReadyCarModelOrderService readyCarModelOrderService;

    @Test
    void shouldCreateOrderAndAssignSalesManagerAutomatically() {
        UUID clientId = UUID.randomUUID();
        UUID carModelId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        CarModel carModel = carModelWithPrices(new Money(1_000_000), new Money(100));
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(employeeId);

        when(carModelRepository.findById(carModelId)).thenReturn(carModel);
        when(employeeAssignment.assignSalesManager()).thenReturn(employee);
        when(orderReadyCarRepository.save(any(OrderReadyCarModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, OrderReadyCarModel.class).getId());

        UUID orderId = readyCarModelOrderService.createOrder(clientId, carModelId);

        ArgumentCaptor<OrderReadyCarModel> savedOrder = ArgumentCaptor.forClass(OrderReadyCarModel.class);
        verify(orderReadyCarRepository).save(savedOrder.capture());
        verify(clientRepository).findById(clientId);
        verify(employeeAssignment).assignSalesManager();

        Assertions.assertEquals(orderId, savedOrder.getValue().getId());
        Assertions.assertEquals(clientId, savedOrder.getValue().getClientId());
        Assertions.assertEquals(employeeId, savedOrder.getValue().getEmployeeId());
        Assertions.assertEquals(ReadyCarOrderStatus.PLACED, savedOrder.getValue().getStatus());
        Assertions.assertEquals(new Money(1_000_600), savedOrder.getValue().getPriceAtCreateOrderMoment());
    }

    @Test
    void shouldThrowExceptionWhenClientIsNotChosen() {
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> readyCarModelOrderService.createOrder(null, UUID.randomUUID())
        );

        verify(orderReadyCarRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCarModelIsNotChosen() {
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> readyCarModelOrderService.createOrder(UUID.randomUUID(), null)
        );

        verify(orderReadyCarRepository, never()).save(any());
    }

    @Test
    void shouldChangeOrderStatusByRoute() {
        UUID orderId = UUID.randomUUID();
        OrderReadyCarModel order = new OrderReadyCarModel(orderId, UUID.randomUUID(), UUID.randomUUID(),
                mock(CarModel.class), new Money(100));

        when(orderReadyCarRepository.findById(orderId)).thenReturn(order);

        readyCarModelOrderService.changeStatus(orderId, ReadyCarOrderStatus.APPROVED_BY_MANAGER);

        Assertions.assertEquals(ReadyCarOrderStatus.APPROVED_BY_MANAGER, order.getStatus());
        verify(orderReadyCarRepository).save(order);
    }

    @Test
    void shouldThrowExceptionWhenStatusRouteIsNotAllowed() {
        UUID orderId = UUID.randomUUID();
        OrderReadyCarModel order = new OrderReadyCarModel(orderId, UUID.randomUUID(), UUID.randomUUID(),
                mock(CarModel.class), new Money(100));

        when(orderReadyCarRepository.findById(orderId)).thenReturn(order);

        Assertions.assertThrows(
                DomainValidationException.class,
                () -> readyCarModelOrderService.changeStatus(orderId, ReadyCarOrderStatus.COMPLETED)
        );

        Assertions.assertEquals(ReadyCarOrderStatus.PLACED, order.getStatus());
        verify(orderReadyCarRepository, never()).save(any());
    }

    @Test
    void shouldDeleteOrder() {
        UUID orderId = UUID.randomUUID();
        when(orderReadyCarRepository.findById(orderId)).thenReturn(mock(OrderReadyCarModel.class));

        readyCarModelOrderService.deleteById(orderId);

        verify(orderReadyCarRepository).deleteById(orderId);
    }

    @Test
    void shouldReturnAllWhenFilterIsEmpty() {
        OrderFilter orderFilter = new OrderFilter();

        OrderReadyCarModel orderReadyCarModel1 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel2 = mock(OrderReadyCarModel.class);

        List<OrderReadyCarModel> orderReadyCarModels = List.of(orderReadyCarModel1, orderReadyCarModel2);

        when(orderReadyCarRepository.findAll()).thenReturn(orderReadyCarModels);

        List<OrderReadyCarModel> result = readyCarModelOrderService.findAll(orderFilter);

        Assertions.assertEquals(orderReadyCarModels.size(), result.size());
        Assertions.assertEquals(orderReadyCarModels, result);
        verify(orderReadyCarRepository).findAll();
    }

    @Test
    void shouldReturnAllWithoutFilter() {
        OrderReadyCarModel order = mock(OrderReadyCarModel.class);
        when(orderReadyCarRepository.findAll()).thenReturn(List.of(order));

        Assertions.assertEquals(List.of(order), readyCarModelOrderService.findAll());
    }

    @Test
    void shouldFilterOrderByClient() {
        UUID clientId1 = UUID.randomUUID();
        UUID clientId2 = UUID.randomUUID();
        UUID clientId3 = UUID.randomUUID();

        OrderFilter orderFilter = new OrderFilter()
                .withClientId(clientId2);

        OrderReadyCarModel orderReadyCarModel1 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel2 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel3 = mock(OrderReadyCarModel.class);

        when(orderReadyCarModel1.getClientId()).thenReturn(clientId1);
        when(orderReadyCarModel2.getClientId()).thenReturn(clientId2);
        when(orderReadyCarModel3.getClientId()).thenReturn(clientId3);

        when(orderReadyCarRepository.findAll()).thenReturn(List.of(orderReadyCarModel1, orderReadyCarModel2, orderReadyCarModel3));

        List<OrderReadyCarModel> expected = List.of(orderReadyCarModel2);

        List<OrderReadyCarModel> result = readyCarModelOrderService.findAll(orderFilter);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void shouldFilterOrderByEmployee() {
        UUID employeeId1 = UUID.randomUUID();
        UUID employeeId2 = UUID.randomUUID();
        UUID employeeId3 = UUID.randomUUID();

        OrderFilter orderFilter = new OrderFilter()
                .withEmployeeId(employeeId2);

        OrderReadyCarModel orderReadyCarModel1 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel2 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel3 = mock(OrderReadyCarModel.class);

        when(orderReadyCarModel1.getEmployeeId()).thenReturn(employeeId1);
        when(orderReadyCarModel2.getEmployeeId()).thenReturn(employeeId2);
        when(orderReadyCarModel3.getEmployeeId()).thenReturn(employeeId3);

        when(orderReadyCarRepository.findAll()).thenReturn(List.of(orderReadyCarModel1, orderReadyCarModel2, orderReadyCarModel3));

        List<OrderReadyCarModel> expected = List.of(orderReadyCarModel2);

        List<OrderReadyCarModel> result = readyCarModelOrderService.findAll(orderFilter);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void shouldFilterOrderByClientAndEmployee() {
        UUID employeeId1 = UUID.randomUUID();
        UUID employeeId2 = UUID.randomUUID();
        UUID employeeId3 = UUID.randomUUID();

        UUID clientId2 = UUID.randomUUID();
        UUID clientId5 = UUID.randomUUID();

        OrderFilter orderFilter = new OrderFilter()
                .withClientId(clientId5)
                .withEmployeeId(employeeId2);

        Assertions.assertEquals(clientId5, orderFilter.getClientId());
        Assertions.assertEquals(employeeId2, orderFilter.getEmployeeId());

        OrderReadyCarModel orderReadyCarModel1 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel2 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel3 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel4 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel5 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel6 = mock(OrderReadyCarModel.class);

        when(orderReadyCarModel1.getEmployeeId()).thenReturn(employeeId1);
        when(orderReadyCarModel2.getEmployeeId()).thenReturn(employeeId2);
        when(orderReadyCarModel3.getEmployeeId()).thenReturn(employeeId3);
        when(orderReadyCarModel4.getEmployeeId()).thenReturn(employeeId1);
        when(orderReadyCarModel5.getEmployeeId()).thenReturn(employeeId2);
        when(orderReadyCarModel6.getEmployeeId()).thenReturn(employeeId3);

        when(orderReadyCarModel2.getClientId()).thenReturn(clientId2);
        when(orderReadyCarModel5.getClientId()).thenReturn(clientId5);

        when(orderReadyCarRepository.findAll()).thenReturn(List.of(orderReadyCarModel1, orderReadyCarModel2,
                orderReadyCarModel3, orderReadyCarModel4, orderReadyCarModel5, orderReadyCarModel6));

        List<OrderReadyCarModel> expected = List.of(orderReadyCarModel5);

        List<OrderReadyCarModel> result = readyCarModelOrderService.findAll(orderFilter);

        Assertions.assertEquals(expected, result);
    }

    private CarModel carModelWithPrices(Money basePrice, Money partPrice) {
        CarModel carModel = mock(CarModel.class);

        Body body = mock(Body.class);
        Engine engine = mock(Engine.class);
        Gearbox gearbox = mock(Gearbox.class);
        SteeringWheel steeringWheel = mock(SteeringWheel.class);
        Interior interior = mock(Interior.class);
        Wheels wheels = mock(Wheels.class);

        when(carModel.getBasePrice()).thenReturn(basePrice);
        when(carModel.getBody()).thenReturn(body);
        when(carModel.getEngine()).thenReturn(engine);
        when(carModel.getGearbox()).thenReturn(gearbox);
        when(carModel.getSteeringWheel()).thenReturn(steeringWheel);
        when(carModel.getInterior()).thenReturn(interior);
        when(carModel.getWheels()).thenReturn(wheels);

        when(body.getPrice()).thenReturn(partPrice);
        when(engine.getPrice()).thenReturn(partPrice);
        when(gearbox.getPrice()).thenReturn(partPrice);
        when(steeringWheel.getPrice()).thenReturn(partPrice);
        when(interior.getPrice()).thenReturn(partPrice);
        when(wheels.getPrice()).thenReturn(partPrice);

        return carModel;
    }
}
