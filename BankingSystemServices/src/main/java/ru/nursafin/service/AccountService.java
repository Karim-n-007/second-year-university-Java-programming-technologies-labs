package ru.nursafin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nursafin.dao.AccountDao;
import ru.nursafin.dao.UserDao;
import ru.nursafin.dto.TransferReceipt;
import ru.nursafin.exception.NotFoundException;
import ru.nursafin.exception.ValidationException;
import ru.nursafin.model.Account;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Operation;
import ru.nursafin.model.OperationType;
import ru.nursafin.money.Money;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final TransferCommissionPolicy transferCommissionPolicy;

    @Transactional
    public Account createAccount(Long ownerId) {
        BankUser owner = requireUser(ownerId);
        return accountDao.save(new Account(owner.getUserId(), owner.getLogin()));
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long accountId) {
        return requireAccount(accountId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long accountId) {
        return requireAccount(accountId).getBalance().getAmount();
    }

    @Transactional
    public Account deposit(Long accountId, Money amount) {
        Account account = requireAccount(accountId);
        account.increaseBalance(amount);
        account.addOperation(new Operation(
                account.getAccountId(),
                OperationType.DEPOSIT,
                amount,
                new Money(),
                account.getBalance(),
                null
        ));

        return accountDao.save(account);
    }

    @Transactional
    public Account withdraw(Long accountId, Money amount) {
        Account account = requireAccount(accountId);
        if (account.getBalance().getAmount().compareTo(amount.getAmount()) < 0) {
            throw new ValidationException("you don't have enough money");
        }

        account.decreaseBalance(amount);
        account.addOperation(new Operation(
                account.getAccountId(),
                OperationType.WITHDRAW,
                amount,
                new Money(),
                account.getBalance(),
                null
        ));

        return accountDao.save(account);
    }

    @Transactional
    public TransferReceipt transfer(Long sourceAccountId, Long targetAccountId, Money amount) {
            if (sourceAccountId.equals(targetAccountId)) {
                throw new ValidationException("Target account the same as source account id");
            }

            Account source = requireAccount(sourceAccountId);
            Account target = requireAccount(targetAccountId);
            BankUser seder = requireUser(source.getOwnerId());
            BankUser receiver = requireUser(target.getOwnerId());

            Money commission = transferCommissionPolicy.calculateCommission(
                    seder,
                    receiver,
                    amount.getAmount()
            );

            Money totalAmount = amount.increase(commission);
            if (source.getBalance().getAmount().compareTo(totalAmount.getAmount()) < 0) {
                throw new ValidationException("Don't have enough balance");
            }

            source.decreaseBalance(totalAmount);
            source.addOperation(new Operation(
                    source.getAccountId(),
                    OperationType.TRANSFER_OUT,
                    amount,
                    commission,
                    source.getBalance(),
                    target.getAccountId()
            ));

            target.increaseBalance(amount);
            target.addOperation(new Operation(
                    target.getAccountId(),
                    OperationType.TRANSFER_IN,
                    amount,
                    new Money(),
                    target.getBalance(),
                    source.getAccountId()
            ));

            Account savedSource = accountDao.save(source);
            Account savedTarget = accountDao.save(target);

            return new TransferReceipt(
                    savedSource.getAccountId(),
                    savedTarget.getAccountId(),
                    amount,
                    commission,
                    totalAmount,
                    source.getBalance(),
                    target.getBalance()
            );
    }

    @Transactional(readOnly = true)
    public List<Operation> getHistory(Long accountId) {
       return requireAccount(accountId).getOperations();
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsByUserId(Long userId) {
            requireUser(userId);
            return accountDao.findByOwnerId(userId);
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountDao.findAll();
    }

    private BankUser requireUser(Long id) {
        BankUser user = userDao.findById(id);
        if (user == null) {
            throw new NotFoundException("user not found");
        }

        return user;
    }

    private Account requireAccount(Long accountId) {
        Account account = accountDao.findById(accountId);
        if (account == null) {
            throw new NotFoundException("account not found");
        }

        return account;
    }
}
