package ru.nursafin.dao;


import jakarta.persistence.EntityManager;
import ru.nursafin.entityManagerContext.EntityManagerContext;
import ru.nursafin.model.Account;

import java.util.List;

public class JpaAccountDao implements AccountDao {
    @Override
    public Account findById(Long id) {
        return getEntityManager()
                .createQuery(
                        "select distinct a from Account a " +
                                "join fetch a.owner " +
                                "left join fetch a.operations " +
                                "where a.accountId = :id",
                        Account.class
                ).setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<Account> findByOwnerId(Long ownerId) {
        return getEntityManager()
                .createQuery(
                        "select a from Account a where a.owner.userId = :ownerId order by a.accountId",
                        Account.class
                )
                .setParameter("ownerId", ownerId)
                .getResultList();
    }

    @Override
    public Account save(Account account) {
        if (account.getAccountId() == null) {
            getEntityManager().persist(account);
            return account;
        }

        return getEntityManager().merge(account);
    }

    @Override
    public List<Account> findAll() {
        return getEntityManager()
                .createQuery(
                        "select a " +
                                "from Account a " +
                                "left join fetch a.owner ",
                        Account.class
                ).getResultList();
    }

    private EntityManager getEntityManager() {
        return EntityManagerContext.getCurrent();
    }
}
