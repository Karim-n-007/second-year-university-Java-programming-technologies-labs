package ru.nursafin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.nursafin.dto.response.BalanceInCurrencyResponse;
import ru.nursafin.exception.RateUnavailableException;
import ru.nursafin.exception.ValidationException;
import ru.nursafin.money.Money;
import ru.nursafin.service.AccountService;
import ru.nursafin.service.RateService;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@Tag(name = "Balance in currency")
public class BalanceController {
    private final AccountService accountService;
    private final RateService rateService;


    @Operation(summary = "Get account balance in chosen currency")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isOwner(#id, authentication)")
    @GetMapping(value = "/{id}/balance", params = "currency")
    public ResponseEntity<?> getBalanceInCurrency(@PathVariable("id") Long id,
                                                  @RequestParam("currency") String currency) {
        if (currency == null || currency.length() != 3) {
            throw new ValidationException("currency must be 3-letter code");
        }

        try {
            BigDecimal rubBalance = accountService.getBalance(id);
            Money converted = rateService.convertFromRub(new Money(rubBalance), currency);

            return ResponseEntity.ok(new BalanceInCurrencyResponse(id, currency.toUpperCase(), converted.getAmount()));
        } catch (RateUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("error", "rate_unavailable", "message", e.getMessage()));
        }
    }
}
