package ru.nursafin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Request to transfer money between accounts")
public class TransferRequest {
    @Schema(description = "Source account id", example = "1")
    @NotNull
    @Positive
    private Long sourceAccountId;

    @Schema(description = "Target account id", example = "2")
    @NotNull
    @Positive
    private Long targetAccountId;

    @Schema(description = "Transfer amount", example = "42.00")
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
