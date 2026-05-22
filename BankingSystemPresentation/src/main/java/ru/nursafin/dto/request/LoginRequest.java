package ru.nursafin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request")
public record LoginRequest(
        @Schema(description = "Login", example = "admin")
        @NotBlank String username,

        @Schema(description = "raw password", example = "password")
        @NotBlank String password
) {
}
