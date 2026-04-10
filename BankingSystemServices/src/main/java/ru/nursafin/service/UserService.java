package ru.nursafin.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import ru.nursafin.dao.UserDao;
import ru.nursafin.dto.UserView;
import ru.nursafin.entityManagerContext.EntityManagerContext;
import ru.nursafin.exception.DuplicateLoginException;
import ru.nursafin.exception.NotFoundException;
import ru.nursafin.model.Account;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private final UserDao userDao;
    private final EntityManagerFactory entityManagerFactory;

    public UserService(UserDao userDao, EntityManagerFactory entityManagerFactory) {
        this.userDao = userDao;
        this.entityManagerFactory = entityManagerFactory;
    }


    public UserView createUser(String login, String name, int age, Gender gender, HairColor hairColor) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            BankUser bankUser = userDao.findByLogin(login);
            if (bankUser != null) {
                throw new DuplicateLoginException("duplicate login");
            }

            BankUser user = new BankUser(login, name, age, gender, hairColor);
            userDao.save(user);

            transaction.commit();
            return toView(user);
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

    public UserView getUser(Long userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            BankUser bankUser = userDao.findById(userId);
            if (bankUser == null) {
                throw new NotFoundException("user not found");
            }
            UserView userView = toView(bankUser);

            transaction.commit();

            return userView;
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

    public UserView addFriend(Long userId, Long friendId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            if (Objects.equals(userId, friendId)) {
                throw new DuplicateLoginException("duplicate login");
            }

            BankUser user = requireUser(userId);
            BankUser friend = requireUser(friendId);

            user.addFriend(friend);
            userDao.save(user);
            userDao.save(friend);

            BankUser bankUser = userDao.findById(userId);
            if (bankUser == null) {
                throw new NotFoundException("user not found");
            }
            UserView userView = toView(bankUser);

            transaction.commit();

            return userView;
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

    public UserView removeFriend(Long userId, Long friendId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            EntityManagerContext.bind(entityManager);
            transaction.begin();

            if (Objects.equals(userId, friendId)) {
                throw new DuplicateLoginException("duplicate login");
            }

            BankUser user = requireUser(userId);
            BankUser friend = requireUser(friendId);

            user.removeFriend(friend);
            userDao.save(user);
            userDao.save(friend);

            BankUser bankUser = userDao.findById(userId);
            if (bankUser == null) {
                throw new NotFoundException("user not found");
            }
            UserView userView = toView(bankUser);

            transaction.commit();

            return userView;
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

    public Set<UserView> getFriends(Long userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            EntityManagerContext.bind(entityManager);

            BankUser bankUser = requireUser(userId);

            return bankUser.getFriends().stream().map(this::toView).collect(Collectors.toSet());

        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }

    public List<UserView> getUsers(Gender gender, HairColor hairColor) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            EntityManagerContext.bind(entityManager);

            return userDao.findAllByFilter(gender, hairColor).stream().map(this::toView).collect(Collectors.toList());
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
    }


    private BankUser requireUser(Long id) {
        BankUser user = userDao.findById(id);
        if (user == null) {
            throw new NotFoundException("user not found");
        }

        return user;
    }

    private UserView toView(BankUser user) {
        Set<String> friendLogins = user.getFriends().stream()
                .map(BankUser::getLogin)
                .collect(Collectors.toSet());

        Set<Long> accountIds = user.getAccounts().stream()
                .map(Account::getAccountId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return new UserView(
                user.getUserId(),
                user.getLogin(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHairColor(),
                friendLogins,
                accountIds
        );
    }
}
