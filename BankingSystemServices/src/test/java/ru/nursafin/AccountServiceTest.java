package ru.nursafin;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nursafin.dao.AccountDao;
import ru.nursafin.dao.UserDao;
import ru.nursafin.dto.AccountView;
import ru.nursafin.dto.TransferReceipt;
import ru.nursafin.exception.ValidationException;
import ru.nursafin.model.Account;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Operation;
import ru.nursafin.model.OperationType;
import ru.nursafin.money.Money;
import ru.nursafin.service.AccountService;
import ru.nursafin.service.TransferCommissionPolicy;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountDao accountDao;

    @Mock
    private UserDao userDao;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        accountService = new AccountService(
                accountDao,
                userDao,
                entityManagerFactory,
                new TransferCommissionPolicy()
        );
    }


    @Test
    void depositShouldIncreaseBalanceAndSaveOperation() {
        BankUser owner = mock(BankUser.class);
        when(owner.getLogin()).thenReturn("login");

        Account account = spy(new Account(owner, new Money(new BigDecimal("100.00"))));
        doReturn(10L).when(account).getAccountId();

        when(accountDao.findById(10L)).thenReturn(account);

        AccountView result = accountService.deposit(10L, new Money(new BigDecimal("25.00")));

        Assertions.assertEquals(0, result.balance().compareTo(new BigDecimal("125.00")));
        Assertions.assertEquals(0, account.getBalance().getAmount().compareTo(new BigDecimal("125.00")));

        Operation operation = account.getOperations().get(0);
        Assertions.assertEquals(OperationType.DEPOSIT, operation.getOperationType());
        Assertions.assertEquals(0, operation.getAmount().getAmount().compareTo(new BigDecimal("25.00")));
        Assertions.assertEquals(0, operation.getCommissionAmount().getAmount().compareTo(BigDecimal.ZERO));
        Assertions.assertEquals(0, operation.getBalanceAfter().getAmount().compareTo(new BigDecimal("125.00")));

        verify(accountDao).save(account);
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void withdrawShouldDecreaseBalanceAndSaveOperation() {
        BankUser owner = mock(BankUser.class);
        when(owner.getLogin()).thenReturn("login");

        Account account = spy(new Account(owner, new Money(new BigDecimal("100.00"))));
        when(account.getAccountId()).thenReturn(10L);
        when(accountDao.findById(10L)).thenReturn(account);

        AccountView result = accountService.withdraw(10L, new Money(new BigDecimal("40.00")));

        Assertions.assertEquals(0, result.balance().compareTo(new BigDecimal("60.00")));
        Assertions.assertEquals(0, account.getBalance().getAmount().compareTo(new BigDecimal("60.00")));
        Assertions.assertEquals(1, account.getOperations().size());

        Operation operation = account.getOperations().get(0);
        Assertions.assertEquals(OperationType.WITHDRAW, operation.getOperationType());
        Assertions.assertEquals(0, operation.getAmount().getAmount().compareTo(new BigDecimal("40.00")));
        Assertions.assertEquals(0, operation.getCommissionAmount().getAmount().compareTo(BigDecimal.ZERO));
        Assertions.assertEquals(0, operation.getBalanceAfter().getAmount().compareTo(new BigDecimal("60.00")));

        verify(accountDao).save(account);
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void withdrawShouldThrowExceptionWhenNotEnoughBalance() {
        BankUser owner = mock(BankUser.class);
        Account account = spy(new Account(owner, new Money(new BigDecimal("100.00"))));

        when(accountDao.findById(10L)).thenReturn(account);
        when(transaction.isActive()).thenReturn(true);

        Assertions.assertThrows(
                ValidationException.class,
                () -> accountService.withdraw(10L, new Money(new BigDecimal("100500.00")))
        );

        verify(accountDao, never()).save(any());
        verify(transaction).rollback();
    }

    @Test
    void transferShouldUseCorrectPercentCommission() {
        BankUser sender = mock(BankUser.class);
        BankUser receiver = mock(BankUser.class);

        when(sender.getUserId()).thenReturn(1L);
        when(receiver.getUserId()).thenReturn(2L);
        when(sender.isFriendWith(receiver)).thenReturn(true);

        Account source = spy(new Account(sender, new Money(new BigDecimal("100.00"))));
        when(source.getAccountId()).thenReturn(10L);

        Account target = spy(new Account(receiver, new Money(BigDecimal.ZERO)));
        when(target.getAccountId()).thenReturn(20L);

        when(accountDao.findById(10L)).thenReturn(source);
        when(accountDao.findById(20L)).thenReturn(target);

        TransferReceipt receipt = accountService.transfer(10L, 20L, new Money(new BigDecimal("50.00")));

        Assertions.assertEquals(0, receipt.amount().getAmount().compareTo(new BigDecimal("50.00")));
        Assertions.assertEquals(0, receipt.commissionAmount().getAmount().compareTo(new BigDecimal("1.50")));
        Assertions.assertEquals(0, receipt.totalDebited().getAmount().compareTo(new BigDecimal("51.50")));

        Assertions.assertEquals(0, source.getBalance().getAmount().compareTo(new BigDecimal("48.50")));
        Assertions.assertEquals(0, target.getBalance().getAmount().compareTo(new BigDecimal("50.00")));

        Assertions.assertEquals(1, source.getOperations().size());
        Assertions.assertEquals(1, target.getOperations().size());

        Operation sourceOperation = source.getOperations().get(0);
        Assertions.assertEquals(OperationType.TRANSFER_OUT, sourceOperation.getOperationType());
        Assertions.assertEquals(0, sourceOperation.getAmount().getAmount().compareTo(new BigDecimal("50.00")));
        Assertions.assertEquals(0, sourceOperation.getCommissionAmount().getAmount().compareTo(new BigDecimal("1.50")));

        Operation targetOperation = target.getOperations().get(0);
        Assertions.assertEquals(OperationType.TRANSFER_IN, targetOperation.getOperationType());
        Assertions.assertEquals(0, targetOperation.getAmount().getAmount().compareTo(new BigDecimal("50.00")));
        Assertions.assertEquals(0, targetOperation.getCommissionAmount().getAmount().compareTo(BigDecimal.ZERO));

        verify(accountDao).save(source);
        verify(accountDao).save(target);
    }
}
