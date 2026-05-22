package ru.nursafin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nursafin.exception.ValidationException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@AllArgsConstructor
public class BankUser {
    private Long userId;

    private String login;

    private String passwordHash;

    private String name;

    private int age;

    private Gender gender;

    private HairColor hairColor;

    private Set<Long> friends = new HashSet<>();

    private Set<Long> accounts = new HashSet<>();

    public BankUser(String login, String passwordHash, String name, int age, Gender gender, HairColor hairColor) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.name = name;
        if (age <= 0) {
            throw new ValidationException("Age must be greater than 0");
        }
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
    }

    public void addFriend(BankUser friend) {
        validateFriend(friend);
        friends.add(friend.getUserId());
        friend.friends.add(userId);
    }

    public void removeFriend(BankUser friend) {
        validateFriend(friend);
        friends.remove(friend.getUserId());
        friend.friends.remove(userId);
    }

    public void addAccountId(Long accountId) {
        if (accountId != null) {
            accounts.add(accountId);
        }
    }

    public boolean isFriendWith(BankUser friend) {
        return friend != null && friend.getUserId() != null &&
        friends.contains(friend.getUserId());
    }

    private void validateFriend(BankUser friend) {
        if (friend == null || friend.getUserId() == null || userId == null) {
            throw new ValidationException("Invalid user");
        }
        if (Objects.equals(userId, friend.getUserId())) {
            throw new ValidationException("You can't be a friend");
        }
    }
}
