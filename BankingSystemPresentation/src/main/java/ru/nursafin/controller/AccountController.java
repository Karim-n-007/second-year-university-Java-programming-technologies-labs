package ru.nursafin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.nursafin.dto.request.AmountRequest;
import ru.nursafin.dto.request.CreateAccountRequest;
import ru.nursafin.dto.request.TransferRequest;
import ru.nursafin.dto.response.AccountResponse;
import ru.nursafin.dto.response.OperationInCurrencyResponse;
import ru.nursafin.dto.response.OperationResponse;
import ru.nursafin.dto.response.TransferResponse;
import ru.nursafin.mapper.ApiMapper;
import ru.nursafin.model.Account;
import ru.nursafin.money.Money;
import ru.nursafin.service.AccountService;
import ru.nursafin.service.RateService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
@Tag(name = "Accounts")
public class AccountController {
    private final AccountService accountService;
    private final ApiMapper apiMapper;
    private final RateService rateService;

    @Operation(summary = "Get account by id. Admin - any, Client - only own")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isOwner(#id, authentication)")
    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable("id") Long id) {
        return apiMapper.toResponse(accountService.getAccount(id));
    }

    @Operation(summary = "Create account for user")
    @ApiResponse(responseCode = "201", description = "Account created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccountResponse createAccount(@RequestBody @Valid CreateAccountRequest request) {
        Account created = accountService.createAccount(request.getOwnerId());
        return apiMapper.toResponse(created);
    }

    @Operation(summary = "Get account balance")
    @ApiResponse(responseCode = "200", description = "Account created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isOwner(#id, authentication)")
    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable("id") Long id) {
        return accountService.getBalance(id);
    }

    @Operation(summary = "Deposit money")
    @ApiResponse(responseCode = "200", description = "Deposit completed")
    @ApiResponse(responseCode = "400", description = "Invalid amount")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PreAuthorize("hasRole('CLIENT') and @accountSecurity.isOwner(#id, authentication)")
    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable("id") Long id, @RequestBody @Valid AmountRequest request) {
        Money money = new Money(request.getAmount());
        return apiMapper.toResponse(accountService.deposit(id, money));
    }

    @Operation(summary = "Withdraw money")
    @ApiResponse(responseCode = "200", description = "Withdrawal completed")
    @ApiResponse(responseCode = "400", description = "Invalid amount or not enough money ")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PreAuthorize("hasRole('CLIENT') and @accountSecurity.isOwner(#id, authentication)")
    @PostMapping("/{id}/withdraw")
    public AccountResponse withdraw(@PathVariable("id") Long id, @RequestBody @Valid AmountRequest request) {
        Money money = new Money(request.getAmount());
        return apiMapper.toResponse(accountService.withdraw(id, money));
    }

    @Operation(summary = "Transfer money between accounts")
    @ApiResponse(responseCode = "200", description = "Transfer completed")
    @ApiResponse(responseCode = "400", description = "Invalid transfer request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PreAuthorize("hasRole('CLIENT') and @accountSecurity.isOwner(#request.sourceAccountId, authentication)")
    @PostMapping("/transfers")
    public TransferResponse transfer(@RequestBody @Valid TransferRequest request) {
        return apiMapper.toResponse(accountService.transfer(
                request.getSourceAccountId(),
                request.getTargetAccountId(),
                new Money(request.getAmount()))
        );
    }

    @Operation(summary = "Get all accounts")
    @ApiResponse(responseCode = "200", description = "Accounts returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts()
                .stream()
                .map(apiMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Get account operation history")
    @ApiResponse(responseCode = "200", description = "History returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/operations")
    public List<OperationResponse> getHistory(@PathVariable("id") Long id) {
        return accountService.getHistory(id)
                .stream()
                .map(apiMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Get account operation history in chosen currency")
    @ApiResponse(responseCode = "200", description = "History returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @ApiResponse(responseCode = "503", description = "currency rate is unavailable")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{id}/operations", params = "currency")
    public ResponseEntity<?> getHistoryInCurrency(@PathVariable("id") Long id, @RequestParam("currency") String currency) {
        if (currency == null || currency.length() != 3) {
            throw new ValidationException("currency must be 3-letter code");
        }

        String code = currency.toUpperCase();

        try {
            List<OperationInCurrencyResponse> history = accountService.getHistory(id)
                    .stream()
                    .map(operation -> toCurrencyResponse(operation, code))
                    .toList();

            return ResponseEntity.ok(history);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    private OperationInCurrencyResponse toCurrencyResponse(ru.nursafin.model.Operation operation, String currency) {
        return new OperationInCurrencyResponse(
                operation.getOperationId(),
                operation.getAccountId(),
                operation.getOperationType(),
                rateService.convertFromRub(operation.getAmount(), currency).getAmount(),
                rateService.convertFromRub(operation.getCommissionAmount(), currency).getAmount(),
                rateService.convertFromRub(operation.getBalanceAfter(), currency).getAmount(),
                operation.getRelatedAccountId(),
                operation.getDateTime(),
                currency);
    }
}
