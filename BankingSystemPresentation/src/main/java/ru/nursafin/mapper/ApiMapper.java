package ru.nursafin.mapper;

import org.springframework.stereotype.Component;
import ru.nursafin.dto.TransferReceipt;
import ru.nursafin.dto.response.AccountResponse;
import ru.nursafin.dto.response.OperationResponse;
import ru.nursafin.dto.response.TransferResponse;
import ru.nursafin.dto.response.UserResponse;
import ru.nursafin.model.Account;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Operation;

@Component
public class ApiMapper {
    public UserResponse toResponse(BankUser user) {
        return new UserResponse(
                user.getUserId(),
                user.getLogin(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHairColor(),
                user.getFriends(),
                user.getAccounts()
        );
    }

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getAccountId(),
                account.getBalance().getAmount(),
                account.getOwnerId(),
                account.getOwnerLogin()
        );
    }

    public OperationResponse toResponse(Operation operation) {
        return new OperationResponse(
                operation.getOperationId(),
                operation.getAccountId(),
                operation.getOperationType(),
                operation.getAmount().getAmount(),
                operation.getCommissionAmount().getAmount(),
                operation.getBalanceAfter().getAmount(),
                operation.getRelatedAccountId(),
                operation.getDateTime()
        );
    }

    public TransferResponse toResponse(TransferReceipt receipt) {
        return new TransferResponse(
                receipt.sourceAccountId(),
                receipt.target(),
                receipt.amount().getAmount(),
                receipt.commissionAmount().getAmount(),
                receipt.totalDebited().getAmount(),
                receipt.balanceAfter().getAmount(),
                receipt.targetBalanceAfter().getAmount()
        );
    }
}
