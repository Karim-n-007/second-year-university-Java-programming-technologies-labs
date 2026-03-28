package ru.nursafin.dao;


import ru.nursafin.model.Account;

import java.util.List;

public interface AccountDao {
    Account findById(Long id);

    List<Account> findByOwnerId(Long ownerId);

    Account save(Account account);
}
