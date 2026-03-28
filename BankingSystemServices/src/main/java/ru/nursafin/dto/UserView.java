package ru.nursafin.dto;

import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

import java.util.Set;

public record UserView(
        Long id,
        String login,
        String name,
        int age,
        Gender gender,
        HairColor hairColor,
        Set<String> friendLogins,
        Set<Long> accountIds
        ) {
}
