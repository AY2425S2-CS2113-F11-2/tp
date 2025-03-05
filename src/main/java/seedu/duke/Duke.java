package seedu.duke;

import java.io.IOException;
import java.util.Scanner;
import seedu.duke.ui.UI;
import seedu.duke.messages.Messages;
import seedu.duke.menu.HelpPage;
import seedu.duke.storage.DataStorage;

public class Duke {
    private DataStorage storage;
    private UI ui;
    private Scanner scanner;
    private Messages messages;
    private HelpPage helpPage;

    public Duke(String fileName) {
        storage = new DataStorage(fileName);
        scanner = new Scanner(System.in);
        messages = new Messages();
        helpPage = new HelpPage();
        ui = new UI(scanner, messages, helpPage, fileName);
        try {
            storage.createFileIfAbsent();
            storage.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Duke("./data/data.txt").run();
    }

    public void run() {
        messages.displayWelcomeMessage();
        helpPage.displayCommandList();
        messages.setDivider();
        ui.handleUserInput();
    }
}

