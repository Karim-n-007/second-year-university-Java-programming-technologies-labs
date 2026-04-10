package ru.nursafin.dao;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.nursafin.exception.NotFoundException;
import ru.nursafin.model.Account;
import ru.nursafin.model.Operation;
import ru.nursafin.persistence.entity.AccountEntity;
import ru.nursafin.persistence.entity.BankUserEntity;
import ru.nursafin.persistence.entity.OperationEntity;
import ru.nursafin.persistence.mapper.PersistenceDomainMapper;
import ru.nursafin.persistence.repository.AccountRepository;
import ru.nursafin.persistence.repository.BankUserRepository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
public class JpaAccountDao implements AccountDao {
    private final AccountRepository accountRepository;
    private final BankUserRepository bankUserRepository;
    private final PersistenceDomainMapper mapper;


    @Override
    public Account findById(Long id) {
        return accountRepository.findDetailedById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<Account> findByOwnerId(Long ownerId) {
        return accountRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity;
        if (account.getAccountId() == null) {
            BankUserEntity owner = bankUserRepository.findById(account.getOwnerId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            entity = new AccountEntity(owner, mapper.toEmbeddableMoney(account.getBalance()));
        }
        else {
            entity = accountRepository.findDetailedById(account.getAccountId()).orElseThrow();
            entity.setBalance(mapper.toEmbeddableMoney(account.getBalance()));
        }


        syncOperations(entity, account.getOperations());


        return mapper.toDomain(accountRepository.save(entity));
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAllWithOwner()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    private void syncOperations(AccountEntity entity, List<Operation> operations) {
        Map<Long, OperationEntity> existingById = new HashMap<>();

        for (OperationEntity operationEntity : entity.getOperations()) {
            if (operationEntity.getOperationId() != null) {
                existingById.put(operationEntity.getOperationId(), operationEntity);
            }
        }

        entity.getOperations().clear();

        for (Operation operation : operations) {
            OperationEntity operationEntity;

            if (operation.getOperationId() != null && existingById.containsKey(operation.getOperationId())) {
                operationEntity = existingById.get(operation.getOperationId());
            } else {
                operationEntity = new OperationEntity();
            }

            setField(operationEntity, "operationType", operation.getOperationType());
            setField(operationEntity, "amount", operation.getAmount());
            setField(operationEntity, "commissionAmount", mapper.toEmbeddableMoney(operation.getCommissionAmount()));
            setField(operationEntity, "balanceAfter", mapper.toEmbeddableMoney(operation.getBalanceAfter()));
            setField(operationEntity, "localDateTime", operation.getDateTime());
            setField(operationEntity, "relatedAccountId", operation.getRelatedAccountId());

            entity.getOperations().add(operationEntity);
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field " + fieldName, e);
        }
    }
}
