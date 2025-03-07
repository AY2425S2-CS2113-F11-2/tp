package seedu.duke.menu;
import seedu.duke.commands.Commands;
import seedu.duke.messages.Messages;

public class HelpPage {

    public void displayCommandList() {
        Messages messages = new Messages();
        messages.setDivider();
        String commandList = Commands.HELP + ": " + Commands.getCommandDescription(Commands.HELP) 
                            + System.lineSeparator()
                            + Commands.CATEGORIES + ": " + Commands.getCommandDescription(Commands.CATEGORIES)
                            + System.lineSeparator()
                            + Commands.EXIT_APP + ": " + Commands.getCommandDescription(Commands.EXIT_APP) 
                            + System.lineSeparator();

        System.out.println("COMMANDS:" + System.lineSeparator());
        System.out.println(commandList);
    }
}
