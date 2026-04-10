package ru.nursafin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nursafin.exception.ValidationException;
import ru.nursafin.money.Money;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Account {
    private final Long accountId;

    private Money balance;

    private final Long ownerId;

    private final String ownerLogin;

    private final List<Operation> operations;

    public Account(Long ownerId, String ownerLogin) {
        this(null, new Money(), ownerId, ownerLogin, new ArrayList<>());
    }

    public Account(Long accountId, Money balance, Long ownerId, String ownerLogin, List<Operation> operations) {
        if (ownerId == null) {
            throw new ValidationException("ownerId is null");
        }
        if (balance == null) {
            throw new ValidationException("balance is null");
        }

        this.accountId = accountId;
        this.balance = balance;
        this.ownerId = ownerId;
        this.ownerLogin = ownerLogin;
        this.operations = operations;
    }

    public void increaseBalance(Money amount) {
        validateAmount(amount);
        balance = balance.increase(amount);
    }

    public void decreaseBalance(Money amount) {
        validateAmount(amount);
        balance = balance.decrease(amount);
    }

    public void addOperation(Operation operation) {
        if (operation == null) {
            throw new ValidationException("Operation must not be null");
        }
        operations.add(operation);
    }

    private void validateAmount(Money amount) {
        if (amount == null) {
            throw new ValidationException("amount must not be null");
        }
    }
}
