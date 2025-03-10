package seedu;

import java.util.Scanner;
import seedu.duke.commands.ExpenseCommand;
import seedu.duke.expense.BudgetManager;
import seedu.duke.menu.HelpPage;
import seedu.duke.messages.Messages;
import seedu.duke.ui.UI;
import seedu.duke.commands.Commands;

public class OMPM {
    /**
     * Main entry-point for the O$P$ application.
     */

    private seedu.duke.ui.UI ui;
    private Scanner scanner;
    private seedu.duke.messages.Messages messages;
    private seedu.duke.menu.HelpPage helpPage;
    private seedu.duke.commands.Commands commands;

    public OMPM () {};

    public static void main(String[] args) {
        new OMPM().run();
    }

    public void run() {
        scanner = new Scanner(System.in);
        messages = new Messages();
        helpPage = new HelpPage();
        commands = new Commands();
        ui = new UI(scanner, messages, helpPage, "data/expenses.txt",
                new ExpenseCommand(new BudgetManager(), scanner), commands);

        messages.displayWelcomeMessage();
        helpPage.displayCommandList();
        messages.setDivider();
        ui.handleUserInput();
    }
}
