package ru.nursafin.dao;

import ru.nursafin.model.Operation;
import ru.nursafin.model.OperationType;

import java.util.List;

public interface OperationDao {
    List<Operation> findByFilter(OperationType operationType, Long AccountId);
}
