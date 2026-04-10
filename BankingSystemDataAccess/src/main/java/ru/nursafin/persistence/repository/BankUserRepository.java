package ru.nursafin.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;
import ru.nursafin.persistence.entity.BankUserEntity;

import java.util.List;
import java.util.Optional;

public interface BankUserRepository extends JpaRepository<BankUserEntity, Long> {

    Optional<BankUserEntity> findByLogin(String login);

    @Query (
            "select distinct u " +
                    "from BankUserEntity u " +
                    "left join fetch u.friends " +
                    "left join fetch u.accounts " +
                    "where u.userId = :id"
    )
    Optional<BankUserEntity> findDetailedById(@Param("id") Long id);


    @Query (
            "select distinct f " +
                    "from BankUserEntity u " +
                    "join u.friends f " +
                    "left join fetch f.friends " +
                    "left join fetch f.accounts " +
                    "where u.userId = :userId " +
                    "order by f.userId"
    )
    List<BankUserEntity> findWithFriendsById(@Param("userId") Long userId);

    @Query(
            "select distinct u " +
                    "from BankUserEntity u " +
                    "left join fetch u.friends " +
                    "left join fetch u.accounts " +
                    "where (:gender is null or u.gender = :gender) " +
                    "and (:hairColor is null or u.hairColor = :hairColor) " +
                    "order by u.userId"
    )
    List<BankUserEntity> findAllByFilter(
            @Param("gender") Gender gender,
            @Param("hairColor") HairColor hairColor
    );
}
