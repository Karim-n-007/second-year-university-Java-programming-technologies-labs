package ru.nursafin.domainModel.entities.testDrive;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum TestDriveRequestStatus {
    REQUESTED,
    CONFIRMED,
    COMPLETED,
    CANCELLED;

    private static final Map<TestDriveRequestStatus, Set<TestDriveRequestStatus>> ALLOWED_TRANSITIONS = Map.of(
            REQUESTED, EnumSet.of(CONFIRMED, CANCELLED),
            CONFIRMED, EnumSet.of(COMPLETED, CANCELLED),
            COMPLETED, EnumSet.noneOf(TestDriveRequestStatus.class),
            CANCELLED, EnumSet.noneOf(TestDriveRequestStatus.class)
    );

    public boolean canChangeTo(TestDriveRequestStatus next) {
        return next != null && allowedNextStatuses().contains(next);
    }

    public Set<TestDriveRequestStatus> allowedNextStatuses() {
        return Collections.unmodifiableSet(ALLOWED_TRANSITIONS.get(this));
    }

    public boolean isFinal() {
        return allowedNextStatuses().isEmpty();
    }
}
