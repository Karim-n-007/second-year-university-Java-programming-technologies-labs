package ru.nursafin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nursafin.dao.OperationDao;
import ru.nursafin.model.Operation;
import ru.nursafin.model.OperationType;

import java.util.List;

@Service
@AllArgsConstructor
public class OperationService {
    private final OperationDao operationDao;

    @Transactional(readOnly = true)
    public List<Operation> getOperationsByFilter(OperationType operationType, Long accountId) {
        return operationDao.findByFilter(operationType, accountId);
    }
}
