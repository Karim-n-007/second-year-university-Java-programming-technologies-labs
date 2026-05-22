package ru.nursafin.security;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.nursafin.model.Role;

import java.util.Collection;
import java.util.List;

@Getter
public class AppPrincipal implements UserDetails {
    private final Long subjectId;
    private final String username;
    private final String passwordHash;
    private final Role role;

    public AppPrincipal(Long subjectId, String username, String passwordHash, Role role) {
        this.subjectId = subjectId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }
}
