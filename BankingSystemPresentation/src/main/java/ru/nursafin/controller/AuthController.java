package ru.nursafin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nursafin.dto.request.LoginRequest;
import ru.nursafin.dto.response.LoginResponse;
import ru.nursafin.security.AppPrincipal;

@RestController
@AllArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Operation(summary = "create http session, login with username and password")
    @ApiResponse(responseCode = "200", description = "Login success")
    @ApiResponse(responseCode = "400", description = "invalid request")
    @ApiResponse(responseCode = "401", description = "invalid credentials")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password());

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);

        AppPrincipal principal = (AppPrincipal) authentication.getPrincipal();
        return new LoginResponse(
                principal.getSubjectId(),
                principal.getUsername(),
                principal.getRole()
        );
    }
}
