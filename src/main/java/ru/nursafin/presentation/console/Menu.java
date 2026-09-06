package ru.nursafin.presentation.console;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Menu {
    protected final ConsoleIo io;

    private final Map<String, MenuItem> items = new LinkedHashMap<>();

    protected Menu(ConsoleIo io) {
        this.io = io;
    }

    public void run() {
        while (true) {
            printItems();

            String choice = io.readLine("Choose an item: ");
            if (choice == null || choice.isBlank() || choice.trim().equals("0")) {
                return;
            }

            MenuItem item = items.get(choice.trim());
            if (item == null) {
                io.print("Unknown menu item: " + choice);
                continue;
            }

            try {
                item.action().run();
            } catch (RuntimeException exception) {
                io.print("Error: " + exception.getMessage());
            }
        }
    }

    protected void register(String key, String title, Runnable action) {
        items.put(key, new MenuItem(title, action));
    }

    protected abstract String title();

    private void printItems() {
        io.print("");
        io.print("=== " + title() + " ===");
        items.forEach((key, item) -> io.print(key + ") " + item.title()));
        io.print("0) back");
    }
}
