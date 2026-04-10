package ru.nursafin.dao;

import ru.nursafin.model.BankUser;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

import java.util.List;

public interface UserDao {
    BankUser findById(Long id);

    BankUser findByLogin(String login);

    BankUser save(BankUser bankUser);

    List<BankUser> findAllByFilter(Gender gender, HairColor hairColor);
}
