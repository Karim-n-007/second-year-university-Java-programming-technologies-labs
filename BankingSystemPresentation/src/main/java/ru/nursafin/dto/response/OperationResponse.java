package ru.nursafin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.nursafin.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Operation response")
public record OperationResponse(
        @Schema(description = "Operation id", example = "3") Long id,
        @Schema(description = "Account id", example = "2") Long accountId,
        @Schema(description = "Operation Type", example = "DEPOSIT") OperationType operationType,
        @Schema(description = "Operation amount", example = "2.00") BigDecimal amount,
        @Schema(description = "Commission amount", example = "1.50") BigDecimal commissionAmount,
        @Schema(description = "balance after operation", example = "5432.54") BigDecimal balanceAfterOperation,
        @Schema(description = "Related account id for transfer", example = "2") Long relatedAccountId,
        @Schema(description = "Operation timestamp", example = "3") LocalDateTime createdAt
        ) {
}
