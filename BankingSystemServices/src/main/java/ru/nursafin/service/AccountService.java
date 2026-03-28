package ru.nursafin.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import ru.nursafin.dao.AccountDao;
import ru.nursafin.dao.UserDao;
import ru.nursafin.dto.AccountView;
import ru.nursafin.dto.OperationView;
import ru.nursafin.dto.TransferReceipt;
import ru.nursafin.entityManagerContext.EntityManagerContext;
import ru.nursafin.exception.DuplicateLoginException;
import ru.nursafin.exception.NotFoundException;
import ru.nursafin.exception.ValidationException;
import ru.nursafin.model.Account;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Operation;
import ru.nursafin.model.OperationType;
import ru.nursafin.money.Money;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final EntityManagerFactory entityManagerFactory;
    private final TransferCommissionPolicy transferCommissionPolicy;

    public AccountService(AccountDao accountDao, UserDao userDao, EntityManagerFactory entityManagerFactory, TransferCommissionPolicy transferCommissionPolicy) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.entityManagerFactory = entityManagerFactory;
        this.transferCommissionPolicy = transferCommissionPolicy;
    }

    public AccountView createAccount(Long ownerId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            BankUser owner = requireUser(ownerId);
            Account account = new Account(owner);
            accountDao.save(account);

            transaction.commit();

            return toView(account);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }

    public AccountView getAccount(Long accountId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            AccountView result = toView(requireAccount(accountId));

            transaction.commit();

            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }

    public BigDecimal getBalance(Long accountId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            BigDecimal result = requireAccount(accountId).getBalance().getAmount();

            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }

    public AccountView deposit(Long accountId, Money amount) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            Account account = requireAccount(accountId);
            account.increaseBalance(amount);
            account.addOperation(new Operation(
                    account,
                    OperationType.DEPOSIT,
                    amount,
                    new Money(),
                    account.getBalance(),
                    null
            ));

            accountDao.save(account);

            transaction.commit();

            return toView(account);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }

    public AccountView withdraw(Long accountId, Money amount) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            Account account = requireAccount(accountId);
            if (account.getBalance().getAmount().compareTo(amount.getAmount()) < 0) {
                throw new ValidationException("Don't have enough balance");
            }

            account.decreaseBalance(amount);

            account.addOperation(new Operation(
                    account,
                    OperationType.WITHDRAW,
                    amount,
                    new Money(),
                    account.getBalance(),
                    null
            ));

            accountDao.save(account);

            transaction.commit();

            return toView(account);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }

    public TransferReceipt transfer(Long sourceAccountId, Long targetAccountId, Money amount) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            if (sourceAccountId.equals(targetAccountId)) {
                throw new DuplicateLoginException("Target account the same as source account id");
            }

            Account source = requireAccount(sourceAccountId);
            Account target = requireAccount(targetAccountId);

            Money commission = transferCommissionPolicy.calculateCommission(
                    source.getOwner(),
                    target.getOwner(),
                    amount.getAmount()
            );

            Money totalAmount = amount.increase(commission);

            if (source.getBalance().getAmount().compareTo(totalAmount.getAmount()) < 0) {
                throw new ValidationException("Don't have enough balance");
            }

            source.decreaseBalance(totalAmount);
            source.addOperation(new Operation(
                    source,
                    OperationType.TRANSFER_OUT,
                    amount,
                    commission,
                    source.getBalance(),
                    target.getAccountId()
            ));

            target.increaseBalance(amount);

            target.addOperation(new Operation(
                    target,
                    OperationType.TRANSFER_IN,
                    amount,
                    new Money(),
                    target.getBalance(),
                    source.getAccountId()
            ));

            accountDao.save(source);
            accountDao.save(target);

            TransferReceipt receipt = new TransferReceipt(
                    source.getAccountId(),
                    target.getAccountId(),
                    amount,
                    commission,
                    totalAmount,
                    source.getBalance(),
                    target.getBalance()
            );

            transaction.commit();

            return receipt;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }

    public List<OperationView> getHistory(Long accountId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            List<OperationView> operationViews = requireAccount(accountId)
                    .getOperations()
                    .stream()
                    .map(this::toView)
                    .toList();

            transaction.commit();

            return operationViews;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }

    private AccountView toView(Account account) {
        return new AccountView(
                account.getAccountId(),
                account.getBalance().getAmount(),
                account.getOwner().getLogin()
        );
    }

    private OperationView toView(Operation operation) {
        return new OperationView(
                operation.getOperationId(),
                operation.getOperationType(),
                operation.getAmount(),
                operation.getCommissionAmount(),
                operation.getBalanceAfter(),
                operation.getRelatedAccountId(),
                operation.getDateTime().toLocalDate()
        );
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
