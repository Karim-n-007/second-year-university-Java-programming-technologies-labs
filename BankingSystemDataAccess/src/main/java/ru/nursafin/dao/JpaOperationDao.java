package ru.nursafin.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.nursafin.model.Operation;
import ru.nursafin.model.OperationType;
import ru.nursafin.persistence.mapper.PersistenceDomainMapper;
import ru.nursafin.persistence.repository.OperationRepository;

import java.util.List;

@Repository
@AllArgsConstructor
public class JpaOperationDao implements OperationDao {
    private final OperationRepository operationRepository;
    private final PersistenceDomainMapper mapper;

    @Override
    public List<Operation> findByFilter(OperationType operationType, Long accountId) {
        return operationRepository.findByFilter(operationType, accountId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
