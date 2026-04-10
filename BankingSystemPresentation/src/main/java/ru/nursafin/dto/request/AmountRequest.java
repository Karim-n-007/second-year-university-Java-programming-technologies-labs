package ru.nursafin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Amount request")
public class AmountRequest {
    @Schema(description = "Positive amount")
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
