package ru.nursafin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

@Getter
@Setter
public class CreateUserRequest {
    @NotBlank
    private String login;

    @NotBlank
    private String name;

    @Min(0)
    private int age;

    @NotNull
    private Gender gender;

    @NotNull
    private HairColor hairColor;
}
