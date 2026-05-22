package ru.nursafin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.nursafin.dto.request.CreateAdminRequest;
import ru.nursafin.dto.response.AdminResponse;
import ru.nursafin.model.Admin;
import ru.nursafin.service.AdminService;

@RestController
@RequestMapping("/api/admins")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admins")
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Create admin")
    @ApiResponse(responseCode = "200", description = "Admin created")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "409", description = "Duplicate login")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AdminResponse createAdmin(@RequestBody @Valid CreateAdminRequest request) {
        Admin admin = adminService.createAdmin(request.getLogin(), request.getPassword());

        return new AdminResponse(admin.getAdminId(), admin.getLogin());
    }
}
