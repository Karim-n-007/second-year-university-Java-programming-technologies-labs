package ru.nursafin.application.services.orders.readyCarModeOrder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderReadyCarModelRepository.OrderReadyCarRepository;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.application.services.orderService.readyCarModelOrderService.ReadyCarModelOrderService;
import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.domainModel.users.client.Client;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadyCarModelOrderServiceTest {
    @Mock
    private OrderReadyCarRepository orderReadyCarRepository;

    @Mock
    ClientRepository clientRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    CarModelRepository carModelRepository;

    @InjectMocks
    ReadyCarModelOrderService readyCarModelOrderService;

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
    void shouldFilterOrderByClient() {
        UUID clientId1 = UUID.randomUUID();
        UUID clientId2 = UUID.randomUUID();
        UUID clientId3 = UUID.randomUUID();


        OrderFilter orderFilter = new OrderFilter()
                                      .withClientId(clientId2);

        OrderReadyCarModel orderReadyCarModel1 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel2 = mock(OrderReadyCarModel.class);
        OrderReadyCarModel orderReadyCarModel3 = mock(OrderReadyCarModel.class);

        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);

        when(orderReadyCarModel1.getClient()).thenReturn(client1);
        when(orderReadyCarModel2.getClient()).thenReturn(client2);
        when(orderReadyCarModel3.getClient()).thenReturn(client3);

        when(client1.getId()).thenReturn(clientId1);
        when(client2.getId()).thenReturn(clientId2);
        when(client3.getId()).thenReturn(clientId3);

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

        Employee employee1 = mock(Employee.class);
        Employee employee2 = mock(Employee.class);
        Employee employee3 = mock(Employee.class);

        when(orderReadyCarModel1.getEmployee()).thenReturn(employee1);
        when(orderReadyCarModel2.getEmployee()).thenReturn(employee2);
        when(orderReadyCarModel3.getEmployee()).thenReturn(employee3);

        when(employee1.getId()).thenReturn(employeeId1);
        when(employee2.getId()).thenReturn(employeeId2);
        when(employee3.getId()).thenReturn(employeeId3);

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

        Employee employee1 = mock(Employee.class);
        Employee employee2 = mock(Employee.class);
        Employee employee3 = mock(Employee.class);

        Client client2 = mock(Client.class);
        Client client5 = mock(Client.class);

        when(orderReadyCarModel1.getEmployee()).thenReturn(employee1);
        when(orderReadyCarModel2.getEmployee()).thenReturn(employee2);
        when(orderReadyCarModel3.getEmployee()).thenReturn(employee3);
        when(orderReadyCarModel4.getEmployee()).thenReturn(employee1);
        when(orderReadyCarModel5.getEmployee()).thenReturn(employee2);
        when(orderReadyCarModel6.getEmployee()).thenReturn(employee3);

        when(orderReadyCarModel2.getClient()).thenReturn(client2);
        when(orderReadyCarModel5.getClient()).thenReturn(client5);

        when(employee1.getId()).thenReturn(employeeId1);
        when(employee2.getId()).thenReturn(employeeId2);
        when(employee3.getId()).thenReturn(employeeId3);

        when(client2.getId()).thenReturn(clientId2);
        when(client5.getId()).thenReturn(clientId5);

        when(orderReadyCarRepository.findAll()).thenReturn(List.of(orderReadyCarModel1, orderReadyCarModel2,
                orderReadyCarModel3, orderReadyCarModel4, orderReadyCarModel5, orderReadyCarModel6));

        List<OrderReadyCarModel> expected = List.of(orderReadyCarModel5);

        List<OrderReadyCarModel> result = readyCarModelOrderService.findAll(orderFilter);

        Assertions.assertEquals(expected, result);
    }

}
