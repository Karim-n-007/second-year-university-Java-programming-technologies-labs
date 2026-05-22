package ru.nursafin.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.nursafin.dao.AccountDao;
import ru.nursafin.model.Account;
import ru.nursafin.model.Role;

import java.util.Objects;

@Component("accountSecurity")
@AllArgsConstructor
public class AccountSecurityService {
    private final AccountDao accountDao;

    public boolean isOwner(Long accountId, Authentication authentication) {
        if (authentication == null || accountId == null) {
            return false;
        }
        if (!(authentication.getPrincipal() instanceof AppPrincipal principal)) {
            return false;
        }
        if (principal.getRole() != Role.CLIENT) {
            return false;
        }

        Account account = accountDao.findById(accountId);
        if (account == null) {
            return false;
        }

        return Objects.equals(account.getOwnerId(), principal.getSubjectId());
    }
}
