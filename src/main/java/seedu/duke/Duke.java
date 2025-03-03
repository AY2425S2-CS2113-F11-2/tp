package seedu.duke;

import java.util.Scanner;
import seedu.duke.ui.UI;
import seedu.duke.messages.Messages;
import seedu.duke.menu.HelpPage;
import seedu.duke.storage.DataStorage;

public class Duke {
    private final String storageFilePath;

    public Duke(String fileName) {
        this.storageFilePath = fileName;  
        DataStorage.ensureFileExists();  
    }

    public static void main(String[] args) {
        new Duke(DataStorage.DATA_FILE).run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        Messages messages = new Messages();
        HelpPage helpPage = new HelpPage();
        UI ui = new UI(scanner, messages, helpPage, storageFilePath);

        messages.displayWelcomeMessage();
        helpPage.displayCommandList();
        messages.setDivider();
        ui.handleUserInput();
    }
}

