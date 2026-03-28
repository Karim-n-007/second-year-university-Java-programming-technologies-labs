package ru.nursafin.dto;

import java.math.BigDecimal;

public record AccountView(Long id,
                          BigDecimal balance,
                          String ownerLogin) {
}
