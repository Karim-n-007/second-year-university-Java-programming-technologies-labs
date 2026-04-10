package ru.nursafin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nursafin.dto.request.AmountRequest;
import ru.nursafin.dto.request.CreateAccountRequest;
import ru.nursafin.dto.request.TransferRequest;
import ru.nursafin.dto.response.AccountResponse;
import ru.nursafin.dto.response.OperationResponse;
import ru.nursafin.dto.response.TransferResponse;
import ru.nursafin.mapper.ApiMapper;
import ru.nursafin.model.Account;
import ru.nursafin.money.Money;
import ru.nursafin.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
@Tag(name = "Accounts")
public class AccountController {
    private final AccountService accountService;
    private final ApiMapper apiMapper;

    @Operation(summary = "Get account by id")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable("id") Long id) {
        return apiMapper.toResponse(accountService.getAccount(id));
    }

    @Operation(summary = "Create account for user")
    @ApiResponse(responseCode = "201", description = "Account created")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccountResponse createAccount(@RequestBody @Valid CreateAccountRequest request) {
        Account created = accountService.createAccount(request.getOwnerId());
        return apiMapper.toResponse(created);
    }

    @Operation(summary = "Get account balance")
    @ApiResponse(responseCode = "200", description = "Account created")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable("id") Long id) {
        return accountService.getBalance(id);
    }

    @Operation(summary = "Deposit money")
    @ApiResponse(responseCode = "200", description = "Deposit completed")
    @ApiResponse(responseCode = "400", description = "Invalid amount")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable("id") Long id, @RequestBody @Valid AmountRequest request) {
        Money money = new Money(request.getAmount());
        return apiMapper.toResponse(accountService.deposit(id, money));
    }

    @Operation(summary = "Withdraw money")
    @ApiResponse(responseCode = "200", description = "Withdrawal completed")
    @ApiResponse(responseCode = "400", description = "Invalid amount or not enough money ")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PostMapping("/{id}/withdraw")
    public AccountResponse withdraw(@PathVariable("id") Long id, @RequestBody @Valid AmountRequest request) {
        Money money = new Money(request.getAmount());
        return apiMapper.toResponse(accountService.withdraw(id, money));
    }

    @Operation(summary = "Transfer money between accounts")
    @ApiResponse(responseCode = "200", description = "Transfer completed")
    @ApiResponse(responseCode = "400", description = "Invalid transfer request")
    @ApiResponse(responseCode = "404", description = "Account not found")
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
    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts()
                .stream()
                .map(apiMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Get account operation history")
    @ApiResponse(responseCode = "200", description = "History returned")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/{id}/operations")
    public List<OperationResponse> getHistory(@PathVariable("id") Long id) {
        return accountService.getHistory(id)
                .stream()
                .map(apiMapper::toResponse)
                .toList();
    }
}
