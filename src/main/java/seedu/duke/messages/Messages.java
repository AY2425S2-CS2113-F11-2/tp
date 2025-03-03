package seedu.duke.messages;

public class Messages {

    public void displayWelcomeMessage() {
        setDivider();
        System.out.println("Welcome to O$P$ expense-tracker! How can I help you?");
    }

    public void setDivider() {
        System.out.println("\n" + "_".repeat(80) + "\n");
    }

    public void invalidCommandMessage() {
        System.out.println("ERROR: Invalid command");
    }

    public void exitAppMessage() {
        System.out.println("Have a nice day!");
    }
}
