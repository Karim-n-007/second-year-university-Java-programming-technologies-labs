package ru.nursafin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Account response")
public record AccountResponse(
        @Schema(description = "Account id", example = "42") Long id,
        @Schema(description = "Current account balance", example = "239.00") BigDecimal balance,
        @Schema(description = "Owner user id", example = "52") Long ownerId,
        @Schema(description = "Owner login", example = "Anatoly") String ownerLogin
) {
}
