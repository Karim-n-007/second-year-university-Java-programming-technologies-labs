package ru.nursafin.application.services.orders.customCarModelOrder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nursafin.application.filters.order.OrderFilter;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderCustomCarRepository.OrderCustomCarRepository;
import ru.nursafin.application.repositories.entitiesRepository.sparePartRepository.*;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.repositories.usersRepository.employeeRepository.EmployeeRepository;
import ru.nursafin.application.services.orderService.customCarModelOrderService.CustomCarModelOrderService;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.order.OrderCustomCarModel;
import ru.nursafin.domainModel.entities.sparePart.body.Body;
import ru.nursafin.domainModel.entities.sparePart.engine.Engine;
import ru.nursafin.domainModel.entities.sparePart.gearbox.Gearbox;
import ru.nursafin.domainModel.entities.sparePart.interior.Interior;
import ru.nursafin.domainModel.entities.sparePart.steeringWheel.SteeringWheel;
import ru.nursafin.domainModel.entities.sparePart.wheels.Wheels;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.IncompatibleComponentException;
import ru.nursafin.domainModel.users.Employee.Employee;
import ru.nursafin.domainModel.users.client.Client;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomCarModelOrderServiceTest {
    @Mock
    private OrderCustomCarRepository orderCustomCarRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CarModelRepository carModelRepository;

    @Mock
    private BodySparePartRepository bodySparePartRepository;

    @Mock
    private EngineSparePartRepository engineSparePartRepository;

    @Mock
    private GearboxSparePartRepository gearboxSparePartRepository;

    @Mock
    private SteeringWheelSparePartRepository steeringWheelSparePartRepository;

    @Mock
    private InteriorSparePartRepository interiorSparePartRepository;

    @Mock
    private WheelsSparePartRepository wheelsSparePartRepository;

    @InjectMocks
    private CustomCarModelOrderService customCarModelOrderService;


    @Test
    void shouldCreateOrder() {
        UUID employeeId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID carModelId = UUID.randomUUID();
        UUID bodyId = UUID.randomUUID();
        UUID engineId = UUID.randomUUID();
        UUID gearboxId = UUID.randomUUID();
        UUID steeringWheelId = UUID.randomUUID();
        UUID interiorId = UUID.randomUUID();
        UUID wheelsId = UUID.randomUUID();
        UUID expectedSavedOrderId = UUID.randomUUID();

        Employee employee = mock(Employee.class);
        Client client = mock(Client.class);
        CarModel carModel = mock(CarModel.class);
        Body body = mock(Body.class);
        Engine engine = mock(Engine.class);
        Interior interior = mock(Interior.class);
        Wheels wheels = mock(Wheels.class);
        SteeringWheel steeringWheel = mock(SteeringWheel.class);
        Gearbox gearbox = mock(Gearbox.class);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);
        when(clientRepository.findById(clientId)).thenReturn(client);
        when(carModelRepository.findById(carModelId)).thenReturn(carModel);
        when(bodySparePartRepository.findById(bodyId)).thenReturn(body);
        when(engineSparePartRepository.findById(engineId)).thenReturn(engine);
        when(gearboxSparePartRepository.findById(gearboxId)).thenReturn(gearbox);
        when(steeringWheelSparePartRepository.findById(steeringWheelId)).thenReturn(steeringWheel);
        when(interiorSparePartRepository.findById(interiorId)).thenReturn(interior);
        when(wheelsSparePartRepository.findById(wheelsId)).thenReturn(wheels);

        when(carModel.getBasePrice()).thenReturn(new Money(100500));
        when(body.getPrice()).thenReturn(new Money(100));
        when(engine.getPrice()).thenReturn(new Money(100));
        when(interior.getPrice()).thenReturn(new Money(100));
        when(wheels.getPrice()).thenReturn(new Money(100));
        when(steeringWheel.getPrice()).thenReturn(new Money(100));
        when(gearbox.getPrice()).thenReturn(new Money(100));

        when(body.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(engine.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(interior.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(wheels.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(steeringWheel.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(gearbox.getCompatibleCars()).thenReturn(Set.of(carModelId));

        when(carModel.getId()).thenReturn(carModelId);

        when(orderCustomCarRepository.save(any(OrderCustomCarModel.class))).thenReturn(expectedSavedOrderId);

        UUID actualSavedOrderId = customCarModelOrderService.createOrder(employeeId, clientId, carModelId, bodyId,
                                                    engineId, gearboxId, steeringWheelId, interiorId, wheelsId );

        Assertions.assertEquals(expectedSavedOrderId, actualSavedOrderId);

        verify(orderCustomCarRepository).save(any(OrderCustomCarModel.class));
        verify(carModelRepository).findById(carModelId);
        verify(bodySparePartRepository).findById(bodyId);
        verify(engineSparePartRepository).findById(engineId);
        verify(gearboxSparePartRepository).findById(gearboxId);
        verify(steeringWheelSparePartRepository).findById(steeringWheelId);
        verify(interiorSparePartRepository).findById(interiorId);
        verify(wheelsSparePartRepository).findById(wheelsId);
    }

    @Test
    void shouldThrowExceptionWhenCreateOrder() {
        UUID employeeId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID carModelId = UUID.randomUUID();
        UUID bodyId = UUID.randomUUID();
        UUID engineId = UUID.randomUUID();
        UUID gearboxId = UUID.randomUUID();
        UUID steeringWheelId = UUID.randomUUID();
        UUID interiorId = UUID.randomUUID();
        UUID wheelsId = UUID.randomUUID();

        UUID fakeCarModelId = UUID.randomUUID();

        Employee employee = mock(Employee.class);
        Client client = mock(Client.class);
        CarModel carModel = mock(CarModel.class);
        Body body = mock(Body.class);
        Engine engine = mock(Engine.class);
        Interior interior = mock(Interior.class);
        Wheels wheels = mock(Wheels.class);
        SteeringWheel steeringWheel = mock(SteeringWheel.class);
        Gearbox gearbox = mock(Gearbox.class);


        when(employeeRepository.findById(employeeId)).thenReturn(employee);
        when(clientRepository.findById(clientId)).thenReturn(client);
        when(carModelRepository.findById(carModelId)).thenReturn(carModel);
        when(bodySparePartRepository.findById(bodyId)).thenReturn(body);
        when(engineSparePartRepository.findById(engineId)).thenReturn(engine);
        when(gearboxSparePartRepository.findById(gearboxId)).thenReturn(gearbox);
        when(steeringWheelSparePartRepository.findById(steeringWheelId)).thenReturn(steeringWheel);
        when(interiorSparePartRepository.findById(interiorId)).thenReturn(interior);
        when(wheelsSparePartRepository.findById(wheelsId)).thenReturn(wheels);

        when(body.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(engine.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(interior.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(wheels.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(steeringWheel.getCompatibleCars()).thenReturn(Set.of(carModelId));
        when(gearbox.getCompatibleCars()).thenReturn(Set.of(fakeCarModelId));

        when(carModel.getId()).thenReturn(carModelId);


        Assertions.assertThrows(
                IncompatibleComponentException.class,
                () -> customCarModelOrderService.createOrder(employeeId, clientId, carModelId, bodyId,
                        engineId, gearboxId, steeringWheelId, interiorId, wheelsId )
        );
    }

    @Test
    void shouldReturnAllWhenFilterIsEmpty() {
        OrderFilter orderFilter = new OrderFilter();

        OrderCustomCarModel orderCustomCarModelModel1 = mock(OrderCustomCarModel.class);
        OrderCustomCarModel orderCustomCarModelModel2 = mock(OrderCustomCarModel.class);

        List<OrderCustomCarModel> orderCustomCarModels = List.of(orderCustomCarModelModel1, orderCustomCarModelModel2);

        when(orderCustomCarRepository.findAll()).thenReturn(orderCustomCarModels);

        List<OrderCustomCarModel> result = customCarModelOrderService.findAll(orderFilter);

        Assertions.assertEquals(orderCustomCarModels.size(), result.size());
        Assertions.assertEquals(orderCustomCarModels, result);
        verify(orderCustomCarRepository).findAll();
    }

    @Test
    void shouldFilterOrderByClient() {
        UUID clientId1 = UUID.randomUUID();
        UUID clientId2 = UUID.randomUUID();
        UUID clientId3 = UUID.randomUUID();

        OrderFilter orderFilter = new OrderFilter()
                .withClientId(clientId2);

        OrderCustomCarModel orderCustomCarModel1 = mock(OrderCustomCarModel.class);
        OrderCustomCarModel orderCustomCarModel2 = mock(OrderCustomCarModel.class);
        OrderCustomCarModel orderCustomCarModel3 = mock(OrderCustomCarModel.class);

        when(orderCustomCarModel1.getClientId()).thenReturn(clientId1);
        when(orderCustomCarModel2.getClientId()).thenReturn(clientId2);
        when(orderCustomCarModel3.getClientId()).thenReturn(clientId3);

        when(orderCustomCarRepository.findAll()).thenReturn(List.of(orderCustomCarModel1, orderCustomCarModel2, orderCustomCarModel3));

        List<OrderCustomCarModel> expected = List.of(orderCustomCarModel2);


        List<OrderCustomCarModel> result = customCarModelOrderService.findAll(orderFilter);


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

        OrderCustomCarModel orderReadyCarModel1 = mock(OrderCustomCarModel.class);
        OrderCustomCarModel orderReadyCarModel2 = mock(OrderCustomCarModel.class);
        OrderCustomCarModel orderReadyCarModel3 = mock(OrderCustomCarModel.class);
        OrderCustomCarModel orderReadyCarModel4 = mock(OrderCustomCarModel.class);
        OrderCustomCarModel orderReadyCarModel5 = mock(OrderCustomCarModel.class);
        OrderCustomCarModel orderReadyCarModel6 = mock(OrderCustomCarModel.class);


        when(orderReadyCarModel1.getEmployeeId()).thenReturn(employeeId1);
        when(orderReadyCarModel2.getEmployeeId()).thenReturn(employeeId2);
        when(orderReadyCarModel3.getEmployeeId()).thenReturn(employeeId3);
        when(orderReadyCarModel4.getEmployeeId()).thenReturn(employeeId1);
        when(orderReadyCarModel5.getEmployeeId()).thenReturn(employeeId2);
        when(orderReadyCarModel6.getEmployeeId()).thenReturn(employeeId3);

        when(orderReadyCarModel2.getClientId()).thenReturn(clientId2);
        when(orderReadyCarModel5.getClientId()).thenReturn(clientId5);

        when(orderCustomCarRepository.findAll()).thenReturn(List.of(orderReadyCarModel1, orderReadyCarModel2,
                orderReadyCarModel3, orderReadyCarModel4, orderReadyCarModel5, orderReadyCarModel6));

        List<OrderCustomCarModel> expected = List.of(orderReadyCarModel5);


        List<OrderCustomCarModel> result = customCarModelOrderService.findAll(orderFilter);


        Assertions.assertEquals(expected, result);
    }
}
