package ru.nursafin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    @NotNull
    private Long sourceAccountId;

    @NotNull
    private Long targetAccountId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
