package seedu.duke.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import seedu.duke.messages.Messages;

public class DataStorage {
    private String fileName;

    public DataStorage(String fileName) {
        this.fileName = fileName;
    }

    public void load() throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file.getAbsolutePath());
        }
    }

    public static List<String> loadData(String filePath) {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println(Messages.errorMessageTag() + e.getMessage());
        }
        return data;
    }

    public static void displayStoredData(String filePath) {
        Messages.loadDataMessage(filePath);
        List<String> data = loadData(filePath);
        if (data.isEmpty()) {
            Messages.emptyDataFileMessage();
        } else {
            for (String line : data) {
                System.out.println(line);
            }
        }
    }

    public void createFileIfAbsent() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            file.createNewFile();
        }
    }
}
