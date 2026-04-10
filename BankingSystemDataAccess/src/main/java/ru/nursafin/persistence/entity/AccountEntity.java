package ru.nursafin.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "accounts")
public class AccountEntity {
    public AccountEntity(BankUserEntity owner, MoneyEmbeddable balance) {
        this.owner = owner;
        this.balance = balance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Setter
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "balance", nullable = false))
    private MoneyEmbeddable balance;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private BankUserEntity owner;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("localDateTime DESC")
    private List<OperationEntity> operations = new ArrayList<>();

    public void addOperation(OperationEntity operation) {
        operations.add(operation);
        operation.setAccount(this);
    }

    public void replaceOperations(List<OperationEntity> newOperations) {
        operations.clear();
        operations.addAll(newOperations);
    }
}
