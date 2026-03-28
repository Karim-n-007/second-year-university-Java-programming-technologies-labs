package ru.nursafin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nursafin.exception.ValidationException;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "bank_users")
public class BankUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false, updatable = false)
    private String login;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "hair_color")
    private HairColor hairColor;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"})
    )
    private Set<BankUser> friends = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Account> accounts = new HashSet<>();

    public BankUser(String login, String name, int age, Gender gender, HairColor hairColor) {
        this.login = login;
        this.name = name;
        if (age <= 0) {
            throw new ValidationException("Age must be greater than 0");
        }
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
    }

    public void addFriend(BankUser friend) {
        if (this.equals(friend)) {
            throw new ValidationException("You can't be a friend");
        }
        friends.add(friend);
        friend.friends.add(this);
    }

    public void removeFriend(BankUser friend) {
        friends.remove(friend);
        friend.friends.remove(this);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public boolean isFriendWith(BankUser friend) {
        return friends.contains(friend);
    }
}
