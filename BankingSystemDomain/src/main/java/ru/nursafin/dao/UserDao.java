package ru.nursafin.dao;

import ru.nursafin.model.BankUser;

public interface UserDao {
    BankUser findById(Long id);

    BankUser findByLogin(String login);

    BankUser save(BankUser bankUser);
}
