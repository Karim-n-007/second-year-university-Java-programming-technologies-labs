package ru.nursafin.domainModel.statuses;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum CustomCarOrderStatus {
    PLACED,
    APPROVED_BY_WAREHOUSE,
    AWAITING_PAYMENT,
    PAID,
    AWAITING_DELIVERY,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED;

    private static final Map<CustomCarOrderStatus, Set<CustomCarOrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            PLACED, EnumSet.of(APPROVED_BY_WAREHOUSE, CANCELLED),
            APPROVED_BY_WAREHOUSE, EnumSet.of(AWAITING_PAYMENT, CANCELLED),
            AWAITING_PAYMENT, EnumSet.of(PAID, CANCELLED),
            PAID, EnumSet.of(AWAITING_DELIVERY),
            AWAITING_DELIVERY, EnumSet.of(READY_FOR_PICKUP),
            READY_FOR_PICKUP, EnumSet.of(COMPLETED),
            COMPLETED, EnumSet.noneOf(CustomCarOrderStatus.class),
            CANCELLED, EnumSet.noneOf(CustomCarOrderStatus.class)
    );

    public boolean canChangeTo(CustomCarOrderStatus next) {
        return next != null && allowedNextStatuses().contains(next);
    }

    public Set<CustomCarOrderStatus> allowedNextStatuses() {
        return Collections.unmodifiableSet(ALLOWED_TRANSITIONS.get(this));
    }

    public boolean isFinal() {
        return allowedNextStatuses().isEmpty();
    }
}
