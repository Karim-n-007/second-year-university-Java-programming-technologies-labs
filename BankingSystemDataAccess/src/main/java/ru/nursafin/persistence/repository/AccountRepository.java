package ru.nursafin.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nursafin.persistence.entity.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query(
            "select distinct a " +
                    "from AccountEntity a " +
                    "join fetch a.owner " +
                    "left join fetch a.operations " +
                    "where a.accountId = :id"
    )
    Optional<AccountEntity> findDetailedById(@Param("id") Long id);

    @Query(
            "select a " +
            "from AccountEntity a " +
            "join fetch a.owner " +
            "where a.owner.userId = :ownerId " +
            "order by a.accountId"
    )
    List<AccountEntity> findAllByOwnerId(@Param("ownerId") Long ownerId);


    @Query (
            "select a " +
            "from AccountEntity a " +
            "join fetch a.owner " +
            "order by a.accountId"
    )
    List<AccountEntity> findAllWithOwner();
}
