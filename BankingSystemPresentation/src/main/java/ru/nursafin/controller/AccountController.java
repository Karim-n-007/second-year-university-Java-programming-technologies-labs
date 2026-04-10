package ru.nursafin.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nursafin.dto.*;
import ru.nursafin.money.Money;
import ru.nursafin.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    public AccountView getAccountById(@PathVariable("id") Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping
    public AccountView createAccount(@RequestBody @Valid CreateAccountRequest request) {
        return accountService.createAccount(request.getOwnerId());
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable("id") Long id) {
        return accountService.getAccount(id).balance();
    }

    @PostMapping("/{id}/deposit")
    public AccountView deposit(@PathVariable("id") Long id, @RequestBody @Valid AmountRequest request) {
        Money money = new Money(request.getAmount());
        return accountService.deposit(id, money);
    }

    @PostMapping("/{id}/withdraw")
    public AccountView withdraw(@PathVariable("id") Long id, @RequestBody @Valid AmountRequest request) {
        Money money = new Money(request.getAmount());
        return accountService.withdraw(id, money);
    }

    @PostMapping("/transfers")
    public TransferReceipt transfer(@RequestBody @Valid TransferRequest request) {
        return accountService.transfer(
                request.getSourceAccountId(),
                request.getTargetAccountId(),
                new Money(request.getAmount())
        );
    }

    @GetMapping
    public List<AccountView> getAllAccounts() {
        return accountService.getAllAccounts();
    }
}
