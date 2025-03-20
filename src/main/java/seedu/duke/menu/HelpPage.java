package seedu.duke.menu;

/**
 * Represents the help page that displays available commands.
 */
public class HelpPage {
    /**
     * Displays the list of available commands.
     */
    public void displayCommandList() {
        System.out.println("AVAILABLE COMMANDS:");
        System.out.println("------------------");
        
        System.out.println("help");
        System.out.println("  Description: Displays this help message");
        System.out.println("  Usage: help");
        System.out.println();
        
        System.out.println("add");
        System.out.println("  Description: Add a new expense");
        System.out.println("  Usage: add");
        System.out.println("  You will be prompted to enter:");
        System.out.println("    - Title: Short name for the expense");
        System.out.println("    - Description: Detailed information about the expense");
        System.out.println("    - Amount: The monetary value (must be a positive number)");
        System.out.println();

        System.out.println("mark");
        System.out.println("  Description: mark an expense as settled");
        System.out.println("  Usage: mark");
        System.out.println("  You will be prompted to enter: expense number");
        System.out.println();

        System.out.println("unmark");
        System.out.println("  Description: unmark an expense to become an unsettled expense");
        System.out.println("  Usage: unmark");
        System.out.println("  You will be prompted to enter: expense number");
        System.out.println();

        System.out.println("list");
        System.out.println("  Description: List all expenses");
        System.out.println("  Usage: list");
        System.out.println();

        System.out.println("list-settled");
        System.out.println("  Description: List all settled expenses");
        System.out.println("  Usage: list-settled");
        System.out.println();

        System.out.println("list-unsettled");
        System.out.println("  Description: List all unsettled expenses");
        System.out.println("  Usage: list-unsettled");
        System.out.println();

        System.out.println("delete");
        System.out.println("  Description: Delete an existing expense");
        System.out.println("  Usage: delete");
        System.out.println("  You will be shown the list of expenses and prompted to enter");
        System.out.println("  the index of the expense to delete");
        System.out.println();
        
        System.out.println("edit");
        System.out.println("  Description: Edit an existing expense");
        System.out.println("  Usage: edit");
        System.out.println("  You will be shown the list of expenses and prompted to enter:");
        System.out.println("    - Index of the expense to edit");
        System.out.println("    - New title (press Enter to keep current)");
        System.out.println("    - New description (press Enter to keep current)");
        System.out.println("    - New amount (press Enter to keep current)");
        System.out.println();
        
        System.out.println("balance");
        System.out.println("  Description: Show the balance overview (total expenses and amount owed)");
        System.out.println("  Usage: balance");
        System.out.println();

        System.out.println("create-group");
        System.out.println("  Description: Create a new group and add members to it");
        System.out.println("  Usage: create-group");
        System.out.println("  You will be prompted to:");
        System.out.println("    - Enter group name");
        System.out.println("    - Enter members to add one by one");
        System.out.println("    - Enter done to create and save the group");
        System.out.println();

        System.out.println("view-group");
        System.out.println("  Description: view the members of a specific group");
        System.out.println("  Usage: view-group");
        System.out.println("  You will be prompted to enter:");
        System.out.println("    - Enter group name");
        System.out.println();

        System.out.println("add-member");
        System.out.println("  Description: add a member to an existing group/ create a new group and add");
        System.out.println("  Usage: add-member");
        System.out.println("  You will be prompted to enter:");
        System.out.println("    - Enter name of new member");
        System.out.println("    - Enter name group to add to");
        System.out.println("        If the group exists, adds to group. " +
                           "Else prompts the user to create a new group first");
        System.out.println();

        System.out.println("remove-group");
        System.out.println("  Description: remove a member from a group");
        System.out.println("  Usage: remove-group");
        System.out.println("  You will be prompted to enter:");
        System.out.println("    - Enter name of member to remove");
        System.out.println("    - Enter group to remove member from");
        System.out.println();

        System.out.println("my-groups");
        System.out.println("  Description: view the members of a specific group");
        System.out.println("  Usage: view-group");
        System.out.println("  Shows all the members and groups");
        System.out.println();
        
        System.out.println("exit");
        System.out.println("  Description: Exit the program");
        System.out.println("  Usage: exit");
    }
}
