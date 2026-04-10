package ru.nursafin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

import java.util.Set;

@Schema(description = "User response")
public record UserResponse(
        @Schema(description = "User id", example = "1") Long id,
        @Schema(description = "User login", example = "igor") String login,
        @Schema(description = "User name", example = "1") String name,
        @Schema(description = "User age", example = "1") int age,
        @Schema(description = "User gender", example = "1") Gender gender,
        @Schema(description = "User hair color", example = "1") HairColor hairColor,
        @Schema(description = "Ids of user friends", example = "1") Set<Long> friendsIds,
        @Schema(description = "Ids of user accounts", example = "1") Set<Long> accountIds
) {
}
