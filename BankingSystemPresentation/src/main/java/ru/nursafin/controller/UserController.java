package ru.nursafin.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.nursafin.dto.AccountView;
import ru.nursafin.dto.CreateUserRequest;
import ru.nursafin.dto.UserView;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;
import ru.nursafin.service.AccountService;
import ru.nursafin.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public UserView getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public UserView createUser(@RequestBody @Valid CreateUserRequest request) {
        return userService.createUser(
                request.getLogin(),
                request.getName(),
                request.getAge(),
                request.getGender(),
                request.getHairColor()
        );
    }

    @GetMapping("/{id}/friends")
    public Set<UserView> getFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/accounts")
    public List<AccountView> getAccounts(@PathVariable long id) {
        return accountService.getAccountsByUserId(id);
    }

    @GetMapping
    public List<UserView> getUsers(
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) HairColor hairColor
    ) {
        return userService.getUsers(gender, hairColor);
    }
}
