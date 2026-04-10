package ru.nursafin.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import ru.nursafin.dao.OperationDao;
import ru.nursafin.dto.OperationView;
import ru.nursafin.entityManagerContext.EntityManagerContext;
import ru.nursafin.model.Operation;
import ru.nursafin.model.OperationType;

import java.util.List;

@AllArgsConstructor
public class OperationService {
    private final OperationDao operationDao;
    private final EntityManagerFactory entityManagerFactory;

    public List<OperationView> getOperationsByFilter(OperationType operationType, Long accountId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            EntityManagerContext.bind(entityManager);

            return operationDao.findByFilter(operationType, accountId)
                    .stream()
                    .map(this::toView)
                    .toList();
        } finally {
            EntityManagerContext.unbind();
            entityManager.close();
        }
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
}
