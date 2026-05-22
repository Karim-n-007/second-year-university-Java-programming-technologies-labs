package ru.nursafin.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.nursafin.model.Role;

import java.util.Objects;

@Component("userSecurity")
public class UserSecurityService {
    public boolean isCurrentUser(Long userId, Authentication authentication) {
        if (authentication == null || userId == null) {
            return false;
        }

        if (!(authentication.getPrincipal() instanceof AppPrincipal principal)) {
            return false;
        }
        if (principal.getRole() != Role.CLIENT) {
            return false;
        }

        return Objects.equals(principal.getSubjectId(), userId);
    }
}
