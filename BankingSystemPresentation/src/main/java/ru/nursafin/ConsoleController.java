package ru.nursafin;

import ru.nursafin.dto.AccountView;
import ru.nursafin.dto.OperationView;
import ru.nursafin.dto.TransferReceipt;
import ru.nursafin.dto.UserView;
import ru.nursafin.model.Gender;
import ru.nursafin.model.HairColor;
import ru.nursafin.money.Money;
import ru.nursafin.service.AccountService;
import ru.nursafin.service.UserService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleController {
    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Выбери пункт меню: ");
            String command = scanner.nextLine().trim();

            try {
                switch (command) {
                    case "1" -> handleCreateUser();
                    case "2" -> handleShowUser();
                    case "3" -> handleAddFriend();
                    case "4" -> handleRemoveFriend();
                    case "5" -> handleCreateAccount();
                    case "6" -> handleShowBalance();
                    case "7" -> handleDeposit();
                    case "8" -> handleWithdraw();
                    case "9" -> handleTransfer();
                    case "10" -> handleShowHistory();
                    case "0" -> {
                        running = false;
                        System.out.println("Выход.");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println();
        }
    }

    private void handleShowHistory() {
        System.out.println("ID счёта: ");
        Long accountId = Long.parseLong(scanner.nextLine());

        List<OperationView> operations = accountService.getHistory(accountId);

        printHistory(accountId, operations);
    }

    private void handleTransfer() {
        System.out.println("ID счёта-источника: ");
        Long sourceId = Long.parseLong(scanner.nextLine());

        System.out.println("ID счёта-получателя: ");
        Long targetId = Long.parseLong(scanner.nextLine());

        System.out.println("Сумма перевода: ");
        BigDecimal amountD= new BigDecimal(scanner.nextLine());
        Money amount = new Money(amountD);

        printTransfer(accountService.transfer(sourceId, targetId, amount));
    }

    private void handleWithdraw() {
        System.out.println("ID счёта: ");
        Long accountId = Long.parseLong(scanner.nextLine());

        System.out.println("Сумма снятия: ");
        BigDecimal amountD= new BigDecimal(scanner.nextLine());
        Money amount = new Money(amountD);

        printAccount(accountService.withdraw(accountId, amount));
    }

    private void handleDeposit() {
        System.out.println("ID счёта: ");
        Long accountId = Long.parseLong(scanner.nextLine());

        System.out.println("Сумма пополнения: ");
        BigDecimal amountD= new BigDecimal(scanner.nextLine());
        Money amount = new Money(amountD);

        printAccount(accountService.deposit(accountId, amount));
    }

    private void handleShowBalance() {
        System.out.println("ID счёта: ");
        Long accountId = Long.parseLong(scanner.nextLine());

        BigDecimal balance = accountService.getBalance(accountId);
        System.out.println("баланс счёта " + accountId + ":" + balance);
    }

    private void handleCreateAccount() {
        System.out.println("ID владельца: ");
        Long ownerId = Long.parseLong(scanner.nextLine());

        printAccount(accountService.createAccount(ownerId));
    }

    private void handleRemoveFriend() {
        System.out.println("ID пользователя: ");
        Long userId = Long.parseLong(scanner.nextLine());

        System.out.println("ID друга: ");
        Long friendId = Long.parseLong(scanner.nextLine());

        printUser(userService.removeFriend(userId, friendId));
    }

    private void handleAddFriend() {
        System.out.println("ID пользователя: ");
        Long userId = Long.parseLong(scanner.nextLine());

        System.out.println("ID друга: ");
        Long friendId = Long.parseLong(scanner.nextLine());

        printUser(userService.addFriend(userId, friendId));
    }

    private void handleShowUser() {
        System.out.println("ID пользователя: ");
        Long userId = Long.parseLong(scanner.nextLine().trim());

        printUser(userService.getUser(userId));
    }

    private void handleCreateUser() {
        System.out.println("логин: ");
        String login = scanner.nextLine().trim();

        System.out.println("имя: ");
        String name = scanner.nextLine().trim();

        System.out.println("Возраст: ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        System.out.println(Arrays.toString(Gender.values()));
        System.out.println("пол: ");
        Gender gender = Gender.valueOf(scanner.nextLine().trim().toUpperCase());

        System.out.println(Arrays.toString(HairColor.values()));
        System.out.println("Цвет волос: ");
        HairColor hairColor = HairColor.valueOf(scanner.nextLine().trim().toUpperCase());

        UserView userView = userService.createUser(login, name, age, gender, hairColor);

        printUser(userView);
    }

    private void printHistory(Long accountId, List<OperationView> operations) {
        System.out.println("История операций счёта " + accountId + ":");
        if (operations.isEmpty()) {
            System.out.println("Истории нет.");
            return;
        }

        for (OperationView operation : operations) {
            System.out.println("-- id: " + operation.id());
            System.out.println("type: " + operation.operationType());
            System.out.println("amount: " + operation.amount().getAmount());
            System.out.println("commission: " + operation.commissionAmount().getAmount());
            System.out.println("balanceAfter: " + operation.balanceAfter().getAmount());
            System.out.println("relatedAccountId: " + operation.relatedAccountId());
            System.out.println("createdAt: " + operation.createdAt());
        }
    }

    private void printTransfer(TransferReceipt transfer) {
        System.out.println("Перевод выполнен: ");
        System.out.println("sourceAccountId: " + transfer.sourceAccountId());
        System.out.println("targetAccountId: " + transfer.target());
        System.out.println("amount: " + transfer.amount().getAmount());
        System.out.println("commissionAmount: " + transfer.commissionAmount().getAmount());
        System.out.println("totalDebited: " + transfer.totalDebited().getAmount());
        System.out.println("sourceBalanceAfter: " + transfer.balanceAfter().getAmount());
        System.out.println("targetBalanceAfter: " + transfer.targetBalanceAfter().getAmount());
    }


    private void printAccount(AccountView account) {
        System.out.println("Счёт: ");
        System.out.println("id = " + account.id());
        System.out.println("owner = " + account.ownerLogin());
        System.out.println("balance = " + account.balance());
    }


    private void printMenu() {
        System.out.println("1 - создать пользователя");
        System.out.println("2 - показать пользователя");
        System.out.println("3 - добавить друга");
        System.out.println("4 - удалить друга");
        System.out.println("5 - создать счёт");
        System.out.println("6 - показать баланс счёта");
        System.out.println("7 - пополнить счёт");
        System.out.println("8 - снять со счёта");
        System.out.println("9 - перевести между счетами");
        System.out.println("10 - показать историю операций счёта");
        System.out.println("0 - выход");
    }

    private void printUser(UserView user) {
        System.out.println("Пользователь: ");
        System.out.println("id = " + user.id());
        System.out.println("login = " + user.login());
        System.out.println("name = " + user.name());
        System.out.println("age = " + user.age());
        System.out.println("gender = " + user.gender());
        System.out.println("hairColor = " + user.hairColor());
        System.out.println("friend = " + user.friendLogins());
        System.out.println("accountIds = " + user.accountIds());

    }
}
