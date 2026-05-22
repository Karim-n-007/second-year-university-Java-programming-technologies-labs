package ru.nursafin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nursafin.dto.request.CreateUserRequest;
import ru.nursafin.dto.response.AccountResponse;
import ru.nursafin.dto.response.UserResponse;
import ru.nursafin.mapper.ApiMapper;
import ru.nursafin.model.BankUser;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;
import ru.nursafin.security.AppPrincipal;
import ru.nursafin.service.AccountService;
import ru.nursafin.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name = "Users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final ApiMapper apiMapper;

    @Operation(summary = "Get info about current authenticated client")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal AppPrincipal principal) {
        return apiMapper.toResponse(userService.getUser(principal.getSubjectId()));
    }


    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id, authentication)")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable long id) {
        return apiMapper.toResponse(userService.getUser(id));
    }

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "409", description = "Duplicate login")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest request) {
        BankUser user = userService.createUser(
                request.getLogin(),
                request.getPassword(),
                request.getName(),
                request.getAge(),
                request.getGender(),
                request.getHairColor()
        );

        return apiMapper.toResponse(user);
    }

    @Operation(summary = "Get user friends")
    @ApiResponse(responseCode = "200", description = "Friends returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id, authentication)")
    @GetMapping("/{id}/friends")
    public Set<UserResponse> getFriends(@PathVariable long id) {
        return userService.getFriends(id)
                .stream()
                .map(apiMapper::toResponse)
                .collect(Collectors.toSet());
    }

    @Operation(summary = "Add friend")
    @ApiResponse(responseCode = "200", description = "Friends returned")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('CLIENT') and @userSecurity.isCurrentUser(#id, authentication)")
    @PostMapping("/{id}/friends/{friendId}")
    public UserResponse addFriend(@PathVariable long id, @PathVariable Long friendId) {
        return apiMapper.toResponse(userService.addFriend(id, friendId));
    }

    @Operation(summary = "Remove friend")
    @ApiResponse(responseCode = "200", description = "Friend removed")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('CLIENT') and @userSecurity.isCurrentUser(#id, authentication)")
    @DeleteMapping("/{id}/friends/{friendId}")
    public UserResponse removeFriend(@PathVariable long id, @PathVariable Long friendId) {
        return apiMapper.toResponse(userService.removeFriend(id, friendId));
    }

    @Operation(summary = "Get accounts of a user")
    @ApiResponse(responseCode = "200", description = "Accounts returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id, authentication)")
    @GetMapping("/{id}/accounts")
    public List<AccountResponse> getAccounts(@PathVariable long id) {
        return accountService.getAccountsByUserId(id)
                .stream()
                .map(apiMapper::toResponse)
                .toList();
    }

    @Operation(summary = "get all users with gender and hairColor filters")
    @ApiResponse(responseCode = "200", description = "Users returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponse> getUsers(
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) HairColor hairColor
    ) {
        return userService.getUsers(gender, hairColor)
                .stream()
                .map(apiMapper::toResponse)
                .toList();
    }
}
