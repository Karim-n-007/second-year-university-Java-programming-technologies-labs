package ru.nursafin.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.nursafin.model.Admin;
import ru.nursafin.persistence.entity.AdminEntity;
import ru.nursafin.persistence.repository.AdminRepository;

@Repository
@AllArgsConstructor
public class JpaAdminDao implements AdminDao {
    private final AdminRepository adminRepository;

    @Override
    public Admin findByLogin(String login) {
        return adminRepository.findByLogin(login)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public Admin save(Admin admin) {
        AdminEntity entity = adminRepository.save(new AdminEntity(admin.getLogin(), admin.getPasswordHash()));
        return toDomain(entity);
    }

    @Override
    public boolean existsAny() {
        return adminRepository.count() > 0;
    }


    private Admin toDomain(AdminEntity entity) {
        return new Admin(entity.getAdminId(), entity.getLogin(), entity.getPasswordHash());
    }
}
