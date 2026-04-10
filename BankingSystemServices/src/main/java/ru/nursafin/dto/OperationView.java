package ru.nursafin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.nursafin.model.OperationType;
import ru.nursafin.money.Money;

import java.time.LocalDate;


@Schema(description = "Operation response")
public record OperationView (
    @Schema(description = "Operation id", example = "1") Long id,
    @Schema(description = "Operation type", example = "DEPOSIT") OperationType operationType,
    @Schema(description = "Amount") Money amount,
    @Schema(description = "Commission amount") Money commissionAmount,
    @Schema(description = "Balance after operation") Money balanceAfter,
    @Schema(description = "Related account id", example = "2") Long relatedAccountId,
    @Schema(description = "Operation date") LocalDate createdAt
) {}
