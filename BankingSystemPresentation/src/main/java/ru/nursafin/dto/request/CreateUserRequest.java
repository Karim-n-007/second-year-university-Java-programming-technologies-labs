package ru.nursafin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

@Getter
@Setter
@Schema(description = "Request to create a user")
public class CreateUserRequest {
    @Schema(description = "Unique user login", example = "igor")
    @NotBlank
    private String login;

    @Schema(description = "user raw password", example = "Agf#42#&sNZ284A")
    @NotBlank
    private String password;

    @Schema(description = "Unique user login", example = "Igor")
    @NotBlank
    private String name;

    @Schema(description = "Unique age", example = "20")
    @Min(0)
    private int age;

    @Schema(description = "User gender", example = "MALE")
    @NotNull
    private Gender gender;

    @Schema(description = "User hair color", example = "BLACK")
    @NotNull
    private HairColor hairColor;
}
