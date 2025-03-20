package seedu;

import java.util.Scanner;
import seedu.duke.commands.ExpenseCommand;
import seedu.duke.commands.FriendsCommands;
import seedu.duke.expense.BudgetManager;
import seedu.duke.friends.GroupManager;
import seedu.duke.menu.HelpPage;
import seedu.duke.messages.Messages;
import seedu.duke.ui.UI;
import seedu.duke.commands.Commands;

public class OMPM {

    private UI ui;
    private Scanner scanner;
    private Messages messages;
    private HelpPage helpPage;
    private Commands commands;

    public OMPM () {
        scanner = new Scanner(System.in);
        messages = new Messages();
        helpPage = new HelpPage();
        commands = new Commands();
    }

    public static void main(String[] args) {
        new OMPM().run();
    }

    public void run() {
        GroupManager groupManager = new GroupManager();  // Create an instance of GroupManager
        FriendsCommands friendsCommand = new FriendsCommands(groupManager);
        ExpenseCommand expenseCommand = new ExpenseCommand(new BudgetManager(), scanner);

        ui = new UI(scanner, messages, helpPage, "data/expenses.txt", expenseCommand, commands, friendsCommand);

        // Display welcome message and command list
        messages.displayWelcomeMessage();
        helpPage.displayCommandList();
        messages.setDivider();

        ui.handleUserInput();
    }
}
