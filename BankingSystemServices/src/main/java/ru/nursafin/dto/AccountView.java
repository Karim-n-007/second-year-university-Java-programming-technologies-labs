package ru.nursafin.dto;



import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Account response")
public record AccountView(
        @Schema(description = "Account id", example = "1") Long id,
                          @Schema(description = "Balance", example = "100.00") BigDecimal balance,
                          @Schema(description = "Owner login", example = "Anatoly") String ownerLogin) {
}
