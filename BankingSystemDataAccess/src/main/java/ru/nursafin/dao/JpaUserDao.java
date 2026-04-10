package ru.nursafin.dao;


import jakarta.persistence.EntityManager;
import ru.nursafin.entityManagerContext.EntityManagerContext;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

import java.util.List;

public class JpaUserDao implements UserDao {
    @Override
    public BankUser findById(Long id) {
        return getEntityManager()
                .createQuery(
                        "select distinct u from BankUser u " +
                                "left join fetch u.friends " +
                                "left join fetch u.accounts " +
                                "where u.userId = :id",
                        BankUser.class
                ).setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public BankUser findByLogin(String login) {
        return getEntityManager()
                .createQuery(
                        "select u from BankUser u where u.login =:login",
                        BankUser.class
                )
                .setParameter("login", login)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public BankUser save(BankUser bankUser) {
        if (bankUser.getUserId() == null) {
            getEntityManager().persist(bankUser);
            return bankUser;
        }

        return getEntityManager().merge(bankUser);
    }

    @Override
    public List<BankUser> findAllByFilter(Gender gender, HairColor hairColor) {
        return getEntityManager()
                .createQuery(
                        "select distinct u " +
                                "from BankUser u " +
                                "left join fetch u.friends " +
                                "left join fetch u.accounts " +
                                "where (:gender is null or u.gender = :gender) " +
                                "and (:hairColor is null or u.hairColor = :hairColor) ",
                        BankUser.class)
                .setParameter("gender", gender)
                .setParameter("hairColor", hairColor)
                .getResultList();
    }


    private EntityManager getEntityManager() {
        return EntityManagerContext.getCurrent();
    }
}
