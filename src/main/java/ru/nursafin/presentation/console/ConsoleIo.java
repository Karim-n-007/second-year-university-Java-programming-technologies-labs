package ru.nursafin.presentation.console;

import ru.nursafin.domainModel.entities.valueObjects.EngineDisplacement;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConsoleIo {
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final BufferedReader reader;
    private final PrintStream writer;

    public ConsoleIo(InputStream input, PrintStream output) {
        this.reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        this.writer = output;
    }

    public void print(String message) {
        writer.println(message);
    }

    public String readLine(String prompt) {
        writer.print(prompt);

        try {
            return reader.readLine();
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public String readRequiredText(String prompt) {
        String value = readLine(prompt);
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Value cannot be empty");
        }

        return value.trim();
    }

    public String readOptionalText(String prompt) {
        String value = readLine(prompt);

        return value == null || value.isBlank() ? null : value.trim();
    }

    public UUID readUuid(String prompt) {
        String value = readRequiredText(prompt);

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw new DomainValidationException("Invalid identifier: " + value);
        }
    }

    public UUID readOptionalUuid(String prompt) {
        String value = readOptionalText(prompt);
        if (value == null) {
            return null;
        }

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw new DomainValidationException("Invalid identifier: " + value);
        }
    }

    public Money readMoney(String prompt) {
        return new Money(readRequiredText(prompt));
    }

    public Money readOptionalMoney(String prompt) {
        String value = readOptionalText(prompt);

        return value == null ? null : new Money(value);
    }

    public Power readOptionalPower(String prompt) {
        Integer value = readOptionalInt(prompt);

        return value == null ? null : new Power(value);
    }

    public EngineDisplacement readOptionalDisplacement(String prompt) {
        Integer value = readOptionalInt(prompt);

        return value == null ? null : new EngineDisplacement(value);
    }

    public int readInt(String prompt) {
        String value = readRequiredText(prompt);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new DomainValidationException("Integer expected, but was: " + value);
        }
    }

    public Integer readOptionalInt(String prompt) {
        String value = readOptionalText(prompt);
        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new DomainValidationException("Integer expected, but was: " + value);
        }
    }

    public <T extends Enum<T>> T readEnum(String prompt, Class<T> enumType) {
        String value = readRequiredText(prompt + " " + describeEnum(enumType) + ": ");

        return parseEnum(enumType, value);
    }

    public <T extends Enum<T>> T readOptionalEnum(String prompt, Class<T> enumType) {
        String value = readOptionalText(prompt + " " + describeEnum(enumType) + ": ");

        return value == null ? null : parseEnum(enumType, value);
    }

    public LocalDateTime readDateTime(String prompt) {
        String value = readRequiredText(prompt + " (dd.MM.yyyy HH:mm): ");

        try {
            return LocalDateTime.parse(value, DATE_TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new DomainValidationException("Invalid date and time: " + value);
        }
    }

    public LocalDate readDate(String prompt) {
        String value = readRequiredText(prompt + " (dd.MM.yyyy): ");

        try {
            return LocalDate.parse(value, DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new DomainValidationException("Invalid date: " + value);
        }
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(constant -> constant.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new DomainValidationException("Invalid value: " + value));
    }

    private <T extends Enum<T>> String describeEnum(Class<T> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining("/", "[", "]"));
    }
}
