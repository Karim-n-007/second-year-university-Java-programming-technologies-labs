package ru.nursafin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

import java.util.Set;

@Schema(description = "User response")
public record UserView(
        @Schema(description = "User id", example = "1") Long id,
        @Schema(description = "User login", example = "igor") String login,
        @Schema(description = "User name", example = "Igor") String name,
        @Schema(description = "User age", example = "20") int age,
        @Schema(description = "User gender", example = "MALE") Gender gender,
        @Schema(description = "Hair color", example = "BROWN") HairColor hairColor,
        @Schema(description = "Friend logins") Set<String> friendLogins,
        @Schema(description = "Account ids") Set<Long> accountIds
        ) {
}
