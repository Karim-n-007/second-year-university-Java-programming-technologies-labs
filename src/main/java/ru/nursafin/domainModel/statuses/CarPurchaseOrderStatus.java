package ru.nursafin.domainModel.statuses;

public sealed interface CarPurchaseOrderStatus
        permits CarPurchaseOrderStatus.HasBeenPlaced,
        CarPurchaseOrderStatus.ApprovedByEmployee,
        CarPurchaseOrderStatus.ReadyForDelivery,
        CarPurchaseOrderStatus.Cancelled,
        CarPurchaseOrderStatus.Completed,
        CarPurchaseOrderStatus.ConfigurationError {

    record HasBeenPlaced() implements CarPurchaseOrderStatus {}

    record ApprovedByEmployee() implements CarPurchaseOrderStatus {}

    record ReadyForDelivery() implements CarPurchaseOrderStatus {}

    record Completed() implements CarPurchaseOrderStatus {}

    record Cancelled() implements CarPurchaseOrderStatus {}

    record ConfigurationError() implements CarPurchaseOrderStatus {}
}
