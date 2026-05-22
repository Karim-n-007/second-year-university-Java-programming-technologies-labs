package ru.nursafin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request to create a user")
public class CreateAdminRequest {
    @Schema(description = "Unique admin login", example = "admin1")
    @NotBlank
    private String login;

    @Schema(description = "admin raw password", example = "adminPassword")
    @NotBlank
    private String password;
}