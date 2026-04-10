package ru.nursafin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response")
public record ErrorResponse (
        @Schema(description = "error code", example = "BAD_REQUEST") String error,
        @Schema(description = "error message", example = "Request body is invalid") String message
) {
}

