package ru.nursafin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.nursafin.model.Role;

@Schema(description = "Login response")
public record LoginResponse (
        @Schema(description = "Authenticated subject id")
        Long subjectId,

        @Schema(description = "Login", example = "admin")
        String username,

        @Schema(description = "Role of authenticated subject", example = "ADMIN")
        Role role
) {
}
