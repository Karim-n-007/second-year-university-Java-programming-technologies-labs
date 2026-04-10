package ru.nursafin.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nursafin.model.OperationType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "operations")
public class OperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long OperationId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
    private MoneyEmbeddable amount;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "commission_amount", nullable = false))
    private MoneyEmbeddable commissionAmount;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "balance_after", nullable = false))
    private MoneyEmbeddable balanceAfter;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @Column(name = "related_account_id")
    private Long relatedAccountId;
}
