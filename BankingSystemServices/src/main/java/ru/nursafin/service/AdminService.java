package ru.nursafin.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nursafin.dao.AdminDao;
import ru.nursafin.exception.DuplicateLoginException;
import ru.nursafin.model.Admin;

@Service
@AllArgsConstructor
public class AdminService {
    private final AdminDao adminDao;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Admin createAdmin(String login, String rawPassword) {
        if (adminDao.findByLogin(login) != null) {
            throw new DuplicateLoginException("duplicate login");
        }

        String hash = passwordEncoder.encode(rawPassword);

        return adminDao.save(new Admin(login, hash));
    }

    @Transactional(readOnly = true)
    public Admin findByLogin(String login) {
        return adminDao.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public boolean existsAny() {
        return adminDao.existsAny();
    }
}
