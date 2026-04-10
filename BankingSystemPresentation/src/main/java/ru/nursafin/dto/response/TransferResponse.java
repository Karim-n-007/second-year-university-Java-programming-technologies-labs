package ru.nursafin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.el.lang.ELArithmetic;

import java.math.BigDecimal;

@Schema(description = "Transfer result")
public record TransferResponse(
        @Schema(description = "Source account id", example = "1") Long sourceAccountId,
        @Schema(description = "Target account id", example = "2") Long targetAccountId,
        @Schema(description = "Transferred amount", example = "50.00") BigDecimal amount,
        @Schema(description = "Commission amount", example = "1.50") BigDecimal commissionAmount,
        @Schema(description = "Total debited from source account", example = "51.50") BigDecimal totalDebitedFromSourceAccount,
        @Schema(description = "Source account balance after transfer", example = "48.50") BigDecimal sourceAccountBalanceAfterTransfer,
        @Schema(description = "Target account balance after transfer", example = "50.00") BigDecimal targetAccountBalanceAfterTransfer
) {
}
