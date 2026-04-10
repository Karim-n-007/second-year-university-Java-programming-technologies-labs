package ru.nursafin.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nursafin.model.OperationType;
import ru.nursafin.persistence.entity.OperationEntity;

import java.util.List;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    @Query(
            "select o " +
            "from OperationEntity o " +
            "join fetch o.account a " +
            "join fetch a.owner " +
            "where (:operationType is null or o.operationType = :operationType) " +
            "and (:accountId is null or o.account.accountId = :accountId) " +
            "order by o.localDateTime desc"
    )
    List<OperationEntity> findByFilter(
            @Param("operationType") OperationType operationType,
            @Param("accountId") Long accountId
    );
}
