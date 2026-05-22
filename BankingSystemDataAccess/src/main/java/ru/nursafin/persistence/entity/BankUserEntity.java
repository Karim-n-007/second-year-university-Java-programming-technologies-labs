package ru.nursafin.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bank_users")
public class BankUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false, updatable = false)
    private String login;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, name = "hair_color")
    @Enumerated(EnumType.STRING)
    private HairColor hairColor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    public BankUserEntity(String login, String passwordHash, String name, int age, Gender gender, HairColor hairColor) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
    }

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"})
    )
    private Set<BankUserEntity> friends = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<AccountEntity> accounts = new HashSet<>();

    public void addFriend(BankUserEntity friend) {
        this.friends.add(friend);
        friend.friends.add(this);
    }

    public void removeFriend(BankUserEntity friend) {
        this.friends.remove(friend);
        friend.friends.remove(this);
    }

    public void addAccount(AccountEntity account) {
        this.accounts.add(account);
        account.setOwner(this);
    }

    public void updateProfile(String name, int age, Gender gender, HairColor hairColor) {
        this.name = name;
        this.age = age;
        this.hairColor = hairColor;
        this.gender = gender;
    }
}
