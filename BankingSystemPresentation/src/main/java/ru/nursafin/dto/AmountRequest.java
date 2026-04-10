package ru.nursafin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AmountRequest {
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
