package ru.nursafin.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.nursafin.service.AdminService;

@Component
@AllArgsConstructor
public class AdminBootstrap implements CommandLineRunner {
    private final AdminService adminService;

    @Override
    public void run(String... args) throws Exception {
        if (!adminService.existsAny()) {
            adminService.createAdmin("admin", "password");
        }
    }
}
