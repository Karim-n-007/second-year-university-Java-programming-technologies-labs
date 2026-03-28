package ru.nursafin;


import jakarta.persistence.EntityManagerFactory;
import ru.nursafin.dao.*;
import ru.nursafin.service.AccountService;
import ru.nursafin.service.TransferCommissionPolicy;
import ru.nursafin.service.UserService;

public class Application {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = null;

        try {
            entityManagerFactory = JpaPersistenceFactory.getEntityManagerFactory();

            UserDao userDao = new JpaUserDao();
            AccountDao accountDao = new JpaAccountDao();

            UserService userService = new UserService(userDao, entityManagerFactory);
            AccountService accountService = new AccountService(
                    accountDao,
                    userDao,
                    entityManagerFactory,
                    new TransferCommissionPolicy()
            );

            ConsoleController consoleController = new ConsoleController(userService, accountService);
            consoleController.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaPersistenceFactory.shutdown();
        }
    }
}
