package ru.nursafin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nursafin.money.Money;


import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "operations")
@NoArgsConstructor
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operationId;

    @ManyToOne()
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
    private Money amount;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "commission_amount", nullable = false))
    private Money commissionAmount;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "balance_after", nullable = false))
    private Money balanceAfter;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "related_account_id")
    private Long relatedAccountId;

    public Operation(
            Account account,
            OperationType operationType,
            Money amount,
            Money commissionAmount,
            Money balanceAfter,
            Long relatedAccountId
    ) {
        this.account = account;
        this.operationType = operationType;
        this.amount = amount;
        this.commissionAmount = commissionAmount;
        this.balanceAfter = balanceAfter;
        this.relatedAccountId = relatedAccountId;
        this.dateTime = LocalDateTime.now();
    }
}
