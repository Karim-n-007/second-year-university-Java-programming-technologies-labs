package ru.nursafin.domainModel.entities.valueObjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

import java.math.BigDecimal;

public class MoneyTest {
    @Test
    void shouldKeepTwoDecimalPlaces() {
        Money money = new Money("100.567");

        Assertions.assertEquals(new BigDecimal("100.57"), money.value());
        Assertions.assertEquals("100.57", money.toString());
    }

    @Test
    void shouldSumMoney() {
        Assertions.assertEquals(new Money(300), new Money(100).plus(new Money(200)));
    }

    @Test
    void shouldSubtractMoney() {
        Assertions.assertEquals(new Money(150), new Money(200).minus(new Money(50)));
    }

    @Test
    void shouldSupportNegativeSurcharge() {
        Money surcharge = new Money(-30_000);

        Assertions.assertTrue(surcharge.isNegative());
        Assertions.assertEquals(new Money(2_970_000), new Money(3_000_000).plus(surcharge));
    }

    @Test
    void shouldCompareMoney() {
        Assertions.assertTrue(new Money(100).compareTo(new Money(200)) < 0);
        Assertions.assertTrue(new Money(300).compareTo(new Money(200)) > 0);
        Assertions.assertEquals(0, new Money(200).compareTo(new Money(200)));
        Assertions.assertFalse(Money.ZERO.isNegative());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNotANumber() {
        Assertions.assertThrows(DomainValidationException.class, () -> new Money("abc"));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        Assertions.assertThrows(DomainValidationException.class, () -> new Money("  "));
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        Assertions.assertThrows(DomainValidationException.class, () -> new Money((BigDecimal) null));
        Assertions.assertThrows(DomainValidationException.class, () -> new Money(100).plus(null));
        Assertions.assertThrows(DomainValidationException.class, () -> new Money(100).minus(null));
    }
}
