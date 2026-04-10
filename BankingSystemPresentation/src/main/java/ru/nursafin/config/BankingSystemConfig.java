package ru.nursafin.config;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nursafin.dao.*;
import ru.nursafin.service.AccountService;
import ru.nursafin.service.OperationService;
import ru.nursafin.service.TransferCommissionPolicy;
import ru.nursafin.service.UserService;

@Configuration
public class BankingSystemConfig {
    @Bean(destroyMethod = "close")
    public EntityManagerFactory entityManagerFactory() {
        return JpaPersistenceFactory.getEntityManagerFactory();
    }

    @Bean
    public UserDao userDao() {
        return new JpaUserDao();
    }

    @Bean
    public AccountDao accountDao() {
        return new JpaAccountDao();
    }

    @Bean
    public OperationDao operationDao() {
        return new JpaOperationDao();
    }

    @Bean
    public TransferCommissionPolicy transferCommissionPolicy() {
        return new TransferCommissionPolicy();
    }

    @Bean
    UserService userService(UserDao userDao, EntityManagerFactory entityManagerFactory) {
        return new UserService(userDao, entityManagerFactory);
    }

    @Bean
    public AccountService accountService(AccountDao accountDao, UserDao userDao, EntityManagerFactory entityManagerFactory, TransferCommissionPolicy transferCommissionPolicy) {
        return new AccountService(accountDao, userDao, entityManagerFactory, transferCommissionPolicy);
    }

    @Bean
    OperationService operationService(OperationDao operationDao, EntityManagerFactory entityManagerFactory) {
        return new OperationService(operationDao, entityManagerFactory);
    }
}