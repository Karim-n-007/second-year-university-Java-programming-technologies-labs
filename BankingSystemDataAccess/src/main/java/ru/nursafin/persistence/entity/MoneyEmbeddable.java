package ru.nursafin.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class MoneyEmbeddable {
    @Column(nullable = false)
    private BigDecimal amount;
}
