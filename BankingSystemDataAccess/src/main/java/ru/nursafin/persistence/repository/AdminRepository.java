package ru.nursafin.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nursafin.persistence.entity.AdminEntity;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByLogin(String login);
}
