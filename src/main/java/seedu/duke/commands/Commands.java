package seedu.duke.commands;

public final class Commands {
    public static final String HELP = "/help";
    public static final String CATEGORIES = "/cats";
    public static final String EXIT_APP = "/bye";
    
    public static String getCommandDescription(String command) {
        return switch (command) {
            case HELP -> "Open a menu of commands";
            case CATEGORIES -> "Shows all the previously created categories";
            case EXIT_APP -> "Save all data and exit the program";
            default -> throw new IllegalArgumentException("ERROR: Invalid command");
        };
    }
}
