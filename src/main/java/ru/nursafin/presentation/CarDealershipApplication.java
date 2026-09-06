package ru.nursafin.presentation;

import ru.nursafin.presentation.console.ConsoleIo;
import ru.nursafin.presentation.console.MainMenu;

public class CarDealershipApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();
        new DemoDataInitializer(context).initialize();

        ConsoleIo io = new ConsoleIo(System.in, System.out);
        io.print("Car dealership information system");

        new MainMenu(io, context).run();

        io.print("Application finished");
    }
}
