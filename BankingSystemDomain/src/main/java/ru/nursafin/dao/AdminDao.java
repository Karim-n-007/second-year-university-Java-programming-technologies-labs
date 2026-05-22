package ru.nursafin.dao;

import ru.nursafin.model.Admin;

public interface AdminDao {
    Admin findByLogin(String login);

    Admin save(Admin admin);

    boolean existsAny();
}
