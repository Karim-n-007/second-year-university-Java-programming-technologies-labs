package ru.nursafin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Admin {
    private final Long adminId;
    private final String login;
    private final String passwordHash;

    public Admin(String login, String passwordHash) {
        adminId = null;
        this.login = login;
        this.passwordHash = passwordHash;
    }
}
