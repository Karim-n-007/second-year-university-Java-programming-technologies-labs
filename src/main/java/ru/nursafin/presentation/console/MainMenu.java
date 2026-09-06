package ru.nursafin.presentation.console;

import ru.nursafin.presentation.ApplicationContext;

public class MainMenu extends Menu {
    public MainMenu(ConsoleIo io, ApplicationContext context) {
        super(io);

        register("1", "Sign in as client", new ClientMenu(io, context)::run);
        register("2", "Sign in as sales manager", new ManagerMenu(io, context)::run);
        register("3", "Sign in as warehouse administrator", new WarehouseAdministratorMenu(io, context)::run);
        register("4", "Sign in as system administrator", new SystemAdministratorMenu(io, context)::run);
    }

    @Override
    protected String title() {
        return "Car dealership";
    }
}
