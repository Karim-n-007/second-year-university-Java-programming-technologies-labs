package ru.nursafin.persistence.mapper;

import org.springframework.stereotype.Component;
import ru.nursafin.model.Account;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Operation;
import ru.nursafin.money.Money;
import ru.nursafin.persistence.entity.AccountEntity;
import ru.nursafin.persistence.entity.BankUserEntity;
import ru.nursafin.persistence.entity.MoneyEmbeddable;
import ru.nursafin.persistence.entity.OperationEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersistenceDomainMapper {

    public BankUser toDomain(BankUserEntity entity) {
        Set<Long> friendIds = entity.getFriends().stream().map(BankUserEntity::getUserId).collect(Collectors.toSet());

        Set<Long> accountIds = entity.getAccounts().stream().map(AccountEntity::getAccountId).collect(Collectors.toSet());

        return new BankUser(
                entity.getUserId(),
                entity.getLogin(),
                entity.getPasswordHash(),
                entity.getName(),
                entity.getAge(),
                entity.getGender(),
                entity.getHairColor(),
                friendIds,
                accountIds
        );
    }

    public Operation toDomain(OperationEntity entity) {
        return new Operation(
                entity.getOperationId(),
                entity.getAccount().getAccountId(),
                entity.getOperationType(),
                toMoney(entity.getAmount()),
                toMoney(entity.getCommissionAmount()),
                toMoney(entity.getBalanceAfter()),
                entity.getLocalDateTime(),
                entity.getRelatedAccountId()
        );
    }

    public Account toDomain(AccountEntity entity) {
        List<Operation> operations = entity.getOperations()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Account(
                entity.getAccountId(),
                toMoney(entity.getBalance()),
                entity.getOwner().getUserId(),
                entity.getOwner().getLogin(),
                operations
        );
    }

    public Money toMoney(MoneyEmbeddable moneyEmbeddable) {
        BigDecimal amount;
        if (moneyEmbeddable == null) {
            amount = BigDecimal.ZERO;
        } else {
            amount = moneyEmbeddable.getAmount();
        }

        return new Money(amount);
    }

    public MoneyEmbeddable toEmbeddableMoney(Money money) {
        return new MoneyEmbeddable(money.getAmount());
    }
}
