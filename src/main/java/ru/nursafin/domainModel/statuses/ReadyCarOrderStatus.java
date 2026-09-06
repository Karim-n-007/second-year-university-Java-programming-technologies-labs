package ru.nursafin.domainModel.statuses;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum ReadyCarOrderStatus {
    PLACED,
    APPROVED_BY_MANAGER,
    AWAITING_PAYMENT,
    PAID,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED;

    private static final Map<ReadyCarOrderStatus, Set<ReadyCarOrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            PLACED, EnumSet.of(APPROVED_BY_MANAGER, CANCELLED),
            APPROVED_BY_MANAGER, EnumSet.of(AWAITING_PAYMENT, CANCELLED),
            AWAITING_PAYMENT, EnumSet.of(PAID, CANCELLED),
            PAID, EnumSet.of(READY_FOR_PICKUP),
            READY_FOR_PICKUP, EnumSet.of(COMPLETED),
            COMPLETED, EnumSet.noneOf(ReadyCarOrderStatus.class),
            CANCELLED, EnumSet.noneOf(ReadyCarOrderStatus.class)
    );

    public boolean canChangeTo(ReadyCarOrderStatus next) {
        return next != null && allowedNextStatuses().contains(next);
    }

    public Set<ReadyCarOrderStatus> allowedNextStatuses() {
        return Collections.unmodifiableSet(ALLOWED_TRANSITIONS.get(this));
    }

    public boolean isFinal() {
        return allowedNextStatuses().isEmpty();
    }
}
