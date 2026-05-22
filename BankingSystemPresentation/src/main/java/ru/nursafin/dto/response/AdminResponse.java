package ru.nursafin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Admin response")
public record AdminResponse(
        @Schema(description = "Admin id", example = "1") Long id,
        @Schema(description = "Admin login", example = "admin") String login
) {
}