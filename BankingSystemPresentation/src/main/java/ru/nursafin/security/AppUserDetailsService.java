package ru.nursafin.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nursafin.model.Admin;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Role;
import ru.nursafin.service.AdminService;
import ru.nursafin.service.UserService;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final AdminService adminService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.findByLogin(username);
        if (admin != null) {
            return new AppPrincipal(admin.getAdminId(), admin.getLogin(), admin.getPasswordHash(), Role.ADMIN);
        }

        BankUser user = userService.findByLogin(username);
        if (user != null) {
            return new AppPrincipal(user.getUserId(), user.getLogin(), user.getPasswordHash(), Role.CLIENT);
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
