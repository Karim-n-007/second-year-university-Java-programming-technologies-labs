package ru.nursafin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.nursafin.money.Money;

@Schema(description = "Transfer result")
public record TransferReceipt(
        @Schema(description = "Source account id", example = "1") Long sourceAccountId,
        @Schema(description = "Target account id", example = "2") Long target,
        @Schema(description = "Transfer amount") Money amount,
        @Schema(description = "Commission amount") Money commissionAmount,
        @Schema(description = "Total debited") Money totalDebited,
        @Schema(description = "Source balance after transfer") Money balanceAfter,
        @Schema(description = "Target balance after transfer") Money targetBalanceAfter
) {
}
