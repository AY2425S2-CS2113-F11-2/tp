package seedu.duke.storage;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import seedu.duke.messages.Messages;

public class DataStorage {
    public static final String DATA_FILE = "C:\\Users\\user\\tp\\src\\main\\java\\seedu\\duke\\storage\\data.txt";

    public static void ensureFileExists() {
        File file = new File(DATA_FILE);
        try {
            if (file.createNewFile()) {
                Messages.createNewFileMessage(file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println(Messages.errorMessageTag() + e.getMessage());
        }
    }

    public static List<String> loadData() {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading storage: " + e.getMessage());
        }
        return data;
    }

    public static void displayStoredData(String filePath) {
        Messages.loadDataMessage(filePath);
        List<String> data = loadData();
        if (data.isEmpty()) {
            Messages.emptyDataFileMessage();
        } else {
            for (String line : data) {
                System.out.println(line);
            }
        }
    }
}
