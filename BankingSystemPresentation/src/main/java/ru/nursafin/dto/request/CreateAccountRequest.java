package ru.nursafin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request to create an account")
public class CreateAccountRequest {
    @Schema(description = "Owner user id", example = "1")
    @NotNull
    @Positive
    private Long ownerId;
}
