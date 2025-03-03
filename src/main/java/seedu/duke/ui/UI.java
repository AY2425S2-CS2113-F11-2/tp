package seedu.duke.ui;
import java.util.Scanner;
import seedu.duke.messages.Messages;
import seedu.duke.commands.Commands;
import seedu.duke.categories.CategoryList;
import seedu.duke.menu.HelpPage;

public class UI {
    private final Scanner scanner;
    private final Messages messages;
    private final HelpPage helpPage;

    public UI (Scanner scanner, Messages messages, HelpPage helpPage) {
        this.scanner = scanner;
        this.messages = messages;
        this.helpPage = helpPage;
    };
    
    public void handleUserInput() {
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase(Commands.HELP)) {
                helpPage.displayCommandList();
                messages.setDivider();
            } else if (input.equalsIgnoreCase(Commands.CATEGORIES)) {
                messages.setDivider();
                
                messages.setDivider();
            } else if (input.contains(Commands.EXIT_APP)) {
                messages.setDivider();
                messages.exitAppMessage();
                messages.setDivider();
                break;
            } else {
                messages.setDivider();
                messages.invalidCommandMessage();
                messages.setDivider();
            }
        }
        scanner.close();
    }
}

