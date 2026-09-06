package ru.nursafin.application.services.testDrive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nursafin.application.filters.testDrive.TestDriveFilter;
import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.application.repositories.entitiesRepository.testDriveRepository.TestDriveCarRepository;
import ru.nursafin.application.repositories.entitiesRepository.testDriveRepository.TestDriveRequestRepository;
import ru.nursafin.application.repositories.usersRepository.clientRepository.ClientRepository;
import ru.nursafin.application.services.testDriveService.TestDriveService;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequest;
import ru.nursafin.domainModel.entities.testDrive.TestDriveRequestStatus;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestDriveServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.of(2025, 5, 20, 12, 0);

    @Mock
    private TestDriveRequestRepository testDriveRequestRepository;

    @Mock
    private TestDriveCarRepository testDriveCarRepository;

    @Mock
    private CarModelRepository carModelRepository;

    @Mock
    private ClientRepository clientRepository;

    private TestDriveService testDriveService;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(NOW.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        testDriveService = new TestDriveService(testDriveRequestRepository, testDriveCarRepository,
                carModelRepository, clientRepository, clock);
    }

    @Test
    void shouldCreateTestDriveRequest() {
        UUID clientId = UUID.randomUUID();
        UUID carModelId = UUID.randomUUID();
        LocalDateTime startDateTime = NOW.plusDays(1);

        when(testDriveCarRepository.contains(carModelId)).thenReturn(true);
        when(testDriveRequestRepository.findAll()).thenReturn(List.of());
        when(testDriveRequestRepository.save(any(TestDriveRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, TestDriveRequest.class).getId());

        UUID requestId = testDriveService.requestTestDrive(clientId, carModelId, startDateTime);

        ArgumentCaptor<TestDriveRequest> savedRequest = ArgumentCaptor.forClass(TestDriveRequest.class);
        verify(testDriveRequestRepository).save(savedRequest.capture());
        verify(clientRepository).findById(clientId);
        verify(carModelRepository).findById(carModelId);

        Assertions.assertEquals(requestId, savedRequest.getValue().getId());
        Assertions.assertEquals(clientId, savedRequest.getValue().getClientId());
        Assertions.assertEquals(carModelId, savedRequest.getValue().getCarModelId());
        Assertions.assertEquals(startDateTime, savedRequest.getValue().getStartDateTime());
        Assertions.assertEquals(TestDriveRequestStatus.REQUESTED, savedRequest.getValue().getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCarIsNotAvailableForTestDrive() {
        UUID carModelId = UUID.randomUUID();
        when(testDriveCarRepository.contains(carModelId)).thenReturn(false);

        Assertions.assertThrows(
                DomainValidationException.class,
                () -> testDriveService.requestTestDrive(UUID.randomUUID(), carModelId, NOW.plusDays(1))
        );

        verify(testDriveRequestRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDateIsInThePast() {
        UUID carModelId = UUID.randomUUID();
        when(testDriveCarRepository.contains(carModelId)).thenReturn(true);

        Assertions.assertThrows(
                DomainValidationException.class,
                () -> testDriveService.requestTestDrive(UUID.randomUUID(), carModelId, NOW.minusHours(1))
        );

        verify(testDriveRequestRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTimeIsAlreadyTaken() {
        UUID carModelId = UUID.randomUUID();
        LocalDateTime startDateTime = NOW.plusDays(2);

        when(testDriveCarRepository.contains(carModelId)).thenReturn(true);
        when(testDriveRequestRepository.findAll()).thenReturn(List.of(
                new TestDriveRequest(UUID.randomUUID(), UUID.randomUUID(), carModelId, startDateTime)));

        Assertions.assertThrows(
                DomainValidationException.class,
                () -> testDriveService.requestTestDrive(UUID.randomUUID(), carModelId, startDateTime)
        );

        verify(testDriveRequestRepository, never()).save(any());
    }

    @Test
    void shouldAllowSameTimeWhenPreviousRequestIsCancelled() {
        UUID carModelId = UUID.randomUUID();
        LocalDateTime startDateTime = NOW.plusDays(2);

        TestDriveRequest cancelled = new TestDriveRequest(UUID.randomUUID(), UUID.randomUUID(), carModelId, startDateTime);
        cancelled.changeStatus(TestDriveRequestStatus.CANCELLED);

        when(testDriveCarRepository.contains(carModelId)).thenReturn(true);
        when(testDriveRequestRepository.findAll()).thenReturn(List.of(cancelled));

        testDriveService.requestTestDrive(UUID.randomUUID(), carModelId, startDateTime);

        verify(testDriveRequestRepository).save(any(TestDriveRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldIsMissing() {
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> testDriveService.requestTestDrive(null, UUID.randomUUID(), NOW.plusDays(1))
        );
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> testDriveService.requestTestDrive(UUID.randomUUID(), null, NOW.plusDays(1))
        );
        Assertions.assertThrows(
                DomainValidationException.class,
                () -> testDriveService.requestTestDrive(UUID.randomUUID(), UUID.randomUUID(), null)
        );
    }

    @Test
    void shouldManageTestDriveCarList() {
        UUID carModelId = UUID.randomUUID();

        testDriveService.addCarToTestDriveList(carModelId);
        verify(carModelRepository).findById(carModelId);
        verify(testDriveCarRepository).add(carModelId);

        testDriveService.removeCarFromTestDriveList(carModelId);
        verify(testDriveCarRepository).remove(carModelId);

        Assertions.assertThrows(DomainValidationException.class,
                () -> testDriveService.addCarToTestDriveList(null));
        Assertions.assertThrows(DomainValidationException.class,
                () -> testDriveService.removeCarFromTestDriveList(null));
    }

    @Test
    void shouldReturnCarsAvailableForTestDriveSortedByBrandAndModel() {
        UUID firstCarId = UUID.randomUUID();
        UUID secondCarId = UUID.randomUUID();

        CarModel audi = mock(CarModel.class);
        CarModel bmw = mock(CarModel.class);
        when(audi.getBrand()).thenReturn("Audi");
        when(bmw.getBrand()).thenReturn("BMW");

        when(testDriveCarRepository.findAll()).thenReturn(Set.of(firstCarId, secondCarId));
        when(carModelRepository.findById(firstCarId)).thenReturn(bmw);
        when(carModelRepository.findById(secondCarId)).thenReturn(audi);

        List<CarModel> result = testDriveService.getCarsAvailableForTestDrive();

        Assertions.assertEquals(List.of(audi, bmw), result);
    }

    @Test
    void shouldFilterRequests() {
        UUID clientId = UUID.randomUUID();
        UUID carModelId = UUID.randomUUID();

        TestDriveRequest matching = new TestDriveRequest(UUID.randomUUID(), clientId, carModelId, NOW.plusDays(1));
        TestDriveRequest anotherClient = new TestDriveRequest(UUID.randomUUID(), UUID.randomUUID(), carModelId,
                NOW.plusDays(2));
        TestDriveRequest anotherCar = new TestDriveRequest(UUID.randomUUID(), clientId, UUID.randomUUID(),
                NOW.plusDays(3));

        when(testDriveRequestRepository.findAll()).thenReturn(List.of(anotherCar, matching, anotherClient));

        List<TestDriveRequest> result = testDriveService.findAll(new TestDriveFilter()
                .withClientId(clientId)
                .withCarModelId(carModelId)
                .withStatus(TestDriveRequestStatus.REQUESTED));

        Assertions.assertEquals(List.of(matching), result);
    }

    @Test
    void shouldReturnAllRequests() {
        TestDriveRequest request = new TestDriveRequest(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                NOW.plusDays(1));
        when(testDriveRequestRepository.findAll()).thenReturn(List.of(request));

        Assertions.assertEquals(List.of(request), testDriveService.findAll());
    }

    @Test
    void shouldChangeRequestStatus() {
        UUID requestId = UUID.randomUUID();
        TestDriveRequest request = new TestDriveRequest(requestId, UUID.randomUUID(), UUID.randomUUID(),
                NOW.plusDays(1));

        when(testDriveRequestRepository.findById(requestId)).thenReturn(request);

        testDriveService.changeStatus(requestId, TestDriveRequestStatus.CONFIRMED);

        Assertions.assertEquals(TestDriveRequestStatus.CONFIRMED, request.getStatus());
        verify(testDriveRequestRepository).save(request);
    }

    @Test
    void shouldThrowExceptionWhenRequestStatusRouteIsNotAllowed() {
        UUID requestId = UUID.randomUUID();
        TestDriveRequest request = new TestDriveRequest(requestId, UUID.randomUUID(), UUID.randomUUID(),
                NOW.plusDays(1));
        request.changeStatus(TestDriveRequestStatus.CANCELLED);

        when(testDriveRequestRepository.findById(requestId)).thenReturn(request);

        Assertions.assertThrows(
                DomainValidationException.class,
                () -> testDriveService.changeStatus(requestId, TestDriveRequestStatus.CONFIRMED)
        );
        Assertions.assertThrows(DomainValidationException.class, () -> request.changeStatus(null));
        Assertions.assertTrue(TestDriveRequestStatus.COMPLETED.isFinal());
        Assertions.assertFalse(TestDriveRequestStatus.REQUESTED.canChangeTo(TestDriveRequestStatus.COMPLETED));
    }

    @Test
    void shouldFindAndDeleteRequest() {
        UUID requestId = UUID.randomUUID();
        TestDriveRequest request = new TestDriveRequest(requestId, UUID.randomUUID(), UUID.randomUUID(),
                NOW.plusDays(1));

        when(testDriveRequestRepository.findById(requestId)).thenReturn(request);

        Assertions.assertEquals(request, testDriveService.findById(requestId));

        testDriveService.deleteById(requestId);
        verify(testDriveRequestRepository).deleteById(requestId);
    }

    @Test
    void shouldRejectRequestWithNullFields() {
        Assertions.assertThrows(DomainValidationException.class,
                () -> new TestDriveRequest(UUID.randomUUID(), null, UUID.randomUUID(), NOW));
    }
}
