package ru.nursafin.domainModel.statuses;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.util.UUID;

import static org.mockito.Mockito.mock;

public class OrderStatusRouteTest {
    @Test
    void readyCarOrderShouldFollowRouteFromSpecification() {
        ReadyCarOrderStatus status = ReadyCarOrderStatus.PLACED;

        Assertions.assertTrue(status.canChangeTo(ReadyCarOrderStatus.APPROVED_BY_MANAGER));
        Assertions.assertTrue(status.canChangeTo(ReadyCarOrderStatus.CANCELLED));
        Assertions.assertFalse(status.canChangeTo(ReadyCarOrderStatus.PAID));
        Assertions.assertFalse(status.canChangeTo(null));

        Assertions.assertTrue(ReadyCarOrderStatus.AWAITING_PAYMENT.canChangeTo(ReadyCarOrderStatus.PAID));
        Assertions.assertTrue(ReadyCarOrderStatus.PAID.canChangeTo(ReadyCarOrderStatus.READY_FOR_PICKUP));
        Assertions.assertTrue(ReadyCarOrderStatus.READY_FOR_PICKUP.canChangeTo(ReadyCarOrderStatus.COMPLETED));
    }

    @Test
    void readyCarOrderShouldNotBeCancelledAfterPayment() {
        Assertions.assertFalse(ReadyCarOrderStatus.PAID.canChangeTo(ReadyCarOrderStatus.CANCELLED));
        Assertions.assertTrue(ReadyCarOrderStatus.COMPLETED.isFinal());
        Assertions.assertTrue(ReadyCarOrderStatus.CANCELLED.isFinal());
        Assertions.assertFalse(ReadyCarOrderStatus.PLACED.isFinal());
    }

    @Test
    void customCarOrderShouldFollowRouteFromSpecification() {
        Assertions.assertTrue(CustomCarOrderStatus.PLACED.canChangeTo(CustomCarOrderStatus.APPROVED_BY_WAREHOUSE));
        Assertions.assertTrue(CustomCarOrderStatus.APPROVED_BY_WAREHOUSE.canChangeTo(CustomCarOrderStatus.AWAITING_PAYMENT));
        Assertions.assertTrue(CustomCarOrderStatus.AWAITING_PAYMENT.canChangeTo(CustomCarOrderStatus.PAID));
        Assertions.assertTrue(CustomCarOrderStatus.PAID.canChangeTo(CustomCarOrderStatus.AWAITING_DELIVERY));
        Assertions.assertTrue(CustomCarOrderStatus.AWAITING_DELIVERY.canChangeTo(CustomCarOrderStatus.READY_FOR_PICKUP));
        Assertions.assertTrue(CustomCarOrderStatus.READY_FOR_PICKUP.canChangeTo(CustomCarOrderStatus.COMPLETED));

        Assertions.assertFalse(CustomCarOrderStatus.PAID.canChangeTo(CustomCarOrderStatus.READY_FOR_PICKUP));
        Assertions.assertFalse(CustomCarOrderStatus.PAID.canChangeTo(CustomCarOrderStatus.CANCELLED));
        Assertions.assertTrue(CustomCarOrderStatus.CANCELLED.isFinal());
        Assertions.assertEquals(2, CustomCarOrderStatus.PLACED.allowedNextStatuses().size());
    }

    @Test
    void orderShouldWalkThroughWholeRoute() {
        OrderReadyCarModel order = new OrderReadyCarModel(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                mock(CarModel.class), new Money(100));

        order.changeStatus(ReadyCarOrderStatus.APPROVED_BY_MANAGER);
        order.changeStatus(ReadyCarOrderStatus.AWAITING_PAYMENT);
        order.changeStatus(ReadyCarOrderStatus.PAID);
        order.changeStatus(ReadyCarOrderStatus.READY_FOR_PICKUP);
        order.changeStatus(ReadyCarOrderStatus.COMPLETED);

        Assertions.assertEquals(ReadyCarOrderStatus.COMPLETED, order.getStatus());
        Assertions.assertThrows(DomainValidationException.class,
                () -> order.changeStatus(ReadyCarOrderStatus.CANCELLED));
    }

    @Test
    void orderShouldRejectNullStatus() {
        OrderReadyCarModel order = new OrderReadyCarModel(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                mock(CarModel.class), new Money(100));

        Assertions.assertThrows(DomainValidationException.class, () -> order.changeStatus(null));
    }

    @Test
    void orderShouldRejectNullFields() {
        Assertions.assertThrows(DomainValidationException.class,
                () -> new OrderReadyCarModel(null, UUID.randomUUID(), UUID.randomUUID(), mock(CarModel.class),
                        new Money(100)));
    }
}
