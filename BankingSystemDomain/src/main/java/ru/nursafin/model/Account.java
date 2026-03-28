package ru.nursafin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nursafin.money.Money;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "balance", nullable = false))
    private Money balance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private BankUser owner;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Operation> operations = new ArrayList<>();

    public Account(BankUser owner) {
        this.balance = new Money();
        this.owner = owner;
        owner.addAccount(this);
    }

    public Account(BankUser owner, Money balance) {
        this.owner = owner;
        this.balance = balance;
        owner.addAccount(this);
    }

    public void increaseBalance(Money amount) {
        this.balance = balance.increase(amount);
    }

    public void decreaseBalance(Money amount) {
        this.balance = balance.decrease(amount);
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
    }
}
