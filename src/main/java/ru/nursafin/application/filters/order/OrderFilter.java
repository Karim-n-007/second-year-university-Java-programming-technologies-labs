package ru.nursafin.application.filters.order;

import java.util.UUID;

public class OrderFilter {
    private UUID employeeId;
    private UUID clientId;

    public OrderFilter withEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public OrderFilter withClientId(UUID clientId) {
        this.clientId = clientId;
        return this;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public UUID getClientId() {
        return clientId;
    }
}
