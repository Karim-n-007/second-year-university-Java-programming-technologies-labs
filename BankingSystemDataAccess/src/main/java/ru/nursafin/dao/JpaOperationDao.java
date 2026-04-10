package ru.nursafin.dao;

import jakarta.persistence.EntityManager;
import ru.nursafin.entityManagerContext.EntityManagerContext;
import ru.nursafin.model.Operation;
import ru.nursafin.model.OperationType;

import java.util.List;

public class JpaOperationDao implements OperationDao {

    @Override
    public List<Operation> findByFilter(OperationType operationType, Long accountId) {
        return getEntityManager()
                .createQuery(
                        "select o " +
                                "from Operation o " +
                                "where (:operationType is null or o.operationType = :operationType) " +
                                "and (:accountId is null or o.account.accountId = :accountId) " +
                                "order by o.dateTime desc",
                        Operation.class)
                .setParameter("operationType", operationType)
                .setParameter("accountId", accountId)
                .getResultList();
    }

    private EntityManager getEntityManager() {
        return EntityManagerContext.getCurrent();
    }
}
