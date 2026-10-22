package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.repository.Repository;

import java.util.Scanner;

public class TextMenu {
    private final Scanner scanner = new Scanner(System.in);
    private int selected = -1;   // index of the selected example, -1 = none

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            if (!scanner.hasNextLine()) {
                break;   // no more input
            }
            String option = scanner.nextLine().trim();
            switch (option) {
                case "1" -> selectProgram();
                case "2" -> runSelectedProgram();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Select a program");
        System.out.println("2. Run the selected program");
        System.out.println("0. Exit");
        if (selected >= 0) {
            System.out.println("(selected: Ex" + (selected + 1) + ")");
        }
        System.out.print("Option: ");
    }

    private void selectProgram() {
        for (int i = 0; i < Examples.ALL.length; i++) {
            System.out.println((i + 1) + ". " + Examples.DESCRIPTIONS[i]);
        }
        System.out.print("Program number: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        try {
            int number = Integer.parseInt(scanner.nextLine().trim());
            if (number < 1 || number > Examples.ALL.length) {
                System.out.println("There is no program with this number.");
            } else {
                selected = number - 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please type a number.");
        }
    }

    private void runSelectedProgram() {
        if (selected < 0) {
            System.out.println("Select a program first.");
            return;
        }
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), Examples.ALL[selected]);
        Controller controller = new Controller(new Repository(state), true);
        try {
            controller.allStep();
        } catch (MyException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
